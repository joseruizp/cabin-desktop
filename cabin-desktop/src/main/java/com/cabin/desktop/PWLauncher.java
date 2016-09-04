package com.cabin.desktop;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import com.cabin.common.PriceUtil;
import com.cabin.common.TimerUtil;
import com.cabin.entity.FormInformation;

public class PWLauncher extends JDialog implements ActionListener {
    private static final long serialVersionUID = -3759856811214634419L;
    public static PWDialog pwScreen = null;
    private static boolean locked = false;
    private static TrayIcon trayIcon;

    private boolean isLoggedIn;
    private FormInformation form;
    private TimerUtil timerUtil;

    private static ViewDetailDialog viewDetailDialog;

    public PWLauncher(TimerUtil timerUtil, FormInformation form) {
        this();
        this.timerUtil = timerUtil;
        setTimer();
        this.isLoggedIn = true;
        this.form = form;
        addViewDetailOption();
        setUnlockedIcon();
    }

    public PWLauncher() {
        initSystemTray();

        setUndecorated(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        setDefaultCloseOperation(2);

        setFocusable(true);
        setVisible(true);
    }

    private void initSystemTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(createImage("images/UnlockedIcon.png"));
        trayIcon.setToolTip("Sistema de Cabinas");
        trayIcon.addActionListener(this);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void addViewDetailOption() {
        MenuItem exitItem = new MenuItem("Ver Detalle");
        final PWLauncher thisDialog = this;
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showViewDetail(thisDialog);
            }
        });

        PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(exitItem);
        trayIcon.setPopupMenu(popupMenu);
    }

    public static Image createImage(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void toggleIcon() {
        locked = !locked;
        final String path;
        if (locked) {
            path = "images/LockedIcon.png";
        } else {
            path = "images/UnlockedIcon.png";
        }
        trayIcon.setImage(createImage(path));
    }

    public static void setUnlockedIcon() {
        trayIcon.setImage(createImage("images/UnlockedIcon.png"));
    }

    public void actionPerformed(ActionEvent arg0) {
        if (!isLoggedIn) {
            blockComputer();
        } else {
            showViewDetail(this);
        }
    }

    private void showViewDetail(PWLauncher launcher) {
        if (viewDetailDialog == null) {
            viewDetailDialog = new ViewDetailDialog(form, timerUtil, launcher);
        } else {
            viewDetailDialog.setVisible(true);
        }
    }

    public void blockComputer() {
        toggleIcon();
        String[] s = null;
        try {
            PWDialog.main(s);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void stopComputer() {
        trayIcon.setImage(createImage("images/LockedIcon.png"));
        try {
            this.timerUtil = null;
            PWDialog.disposeInstance();
            PWDialog.main(null);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void showNotification(FormInformation form, double totalTime) {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        new NotificationDialog(TimerUtil.getHoursAsString(totalTime), form);
    }

    private static void updateNotificationDialog(FormInformation form, String totalTime) {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        new NotificationDialog(totalTime, form);
    }

    public void setTimer() {
        this.timerUtil.replaceActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("action listener launcher, " + timerUtil.getRemainingTime());
                System.out.println("action listener launcher viewDetail visible, " + viewDetailDialog.isVisible());
                form.updateBalance(timerUtil.getRemainingTime());
                if (timerUtil.isShowNotification()) {
                    viewDetailDialog.setVisible(false);
                    updateNotificationDialog(form, timerUtil.getRemainingTime());
                } else if (timerUtil.isOver()) {
                    viewDetailDialog.dispose();
                    timerUtil.stop();
                    blockComputer();
                } else if (viewDetailDialog.isVisible()) {
                    Double price = form.getClient().getBalance();
                    viewDetailDialog.getBalanceValueLabel().setText(String.valueOf(PriceUtil.round(price)));
                }
            }
        });
    }

    public void extendTime(double hours) {
        this.timerUtil.extendTime(hours);
    }

    public static void main(String[] args) {
        new PWLauncher();
    }

}