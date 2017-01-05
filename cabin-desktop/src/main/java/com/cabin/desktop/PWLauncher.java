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

import org.apache.log4j.Logger;

import com.cabin.common.PriceUtil;
import com.cabin.common.TimerUtil;
import com.cabin.entity.FormInformation;

public class PWLauncher extends JDialog implements ActionListener {
	
	final static Logger logger = Logger.getLogger(PWLauncher.class);
	
    private static final long serialVersionUID = -3759856811214634419L;
    public static PWDialog pwScreen = null;
    private static boolean locked = false;
    private static TrayIcon trayIcon;

    private static boolean isLoggedIn;
    private static FormInformation form;
    private static TimerUtil timerUtil;

    private static PWLauncher instance;

    private static ViewDetailDialog viewDetailDialog;

    public PWLauncher() {
    	logger.info("starting app");
        initSystemTray(this);
        addViewDetailOption();

        setUndecorated(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        setDefaultCloseOperation(2);

        setFocusable(true);
        setVisible(true);
        instance = this;
    }

    private static void initSystemTray(ActionListener actionListener) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(createImage("images/UnlockedIcon.png"));
        trayIcon.setToolTip("Sistema de Cabinas");
        trayIcon.addActionListener(actionListener);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static void addViewDetailOption() {
        MenuItem exitItem = new MenuItem("Ver Detalle");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showViewDetail(instance);
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

    private static void showViewDetail(PWLauncher launcher) {
        if (viewDetailDialog == null) {
            viewDetailDialog = new ViewDetailDialog(form, timerUtil, launcher);
        } else {
            viewDetailDialog.setVisible(true);
        }
    }

    public static void blockComputer() {
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
        isLoggedIn = false;
        try {
            timerUtil = null;
            PWDialog.disposeInstance();
            PWDialog.main(null);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void showNotification(FormInformation form, double totalTime) {
        PWLauncher.form = form;
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        new NotificationDialog(TimerUtil.getHoursAsString(totalTime), form.getClient().getBalance(), form.getTariff());
    }

    private static void updateNotificationDialog(String totalTime) {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        new NotificationDialog(totalTime, form.getClient().getBalance(), form.getTariff());
    }

    public static void setTimer() {
        timerUtil.replaceActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("action listener launcher, " + timerUtil.getRemainingTime());
                form.updateBalance(timerUtil.getRemainingTime());
                if (timerUtil.isShowNotification()) {
                    if (viewDetailDialog != null) {
                        viewDetailDialog.setVisible(false);
                    }
                    updateNotificationDialog(timerUtil.getRemainingTime());
                } else if (timerUtil.isOver()) {
                    if (viewDetailDialog != null) {
                        viewDetailDialog.dispose();
                    }
                    timerUtil.stop();
                    blockComputer();
                } else if (viewDetailDialog != null && viewDetailDialog.isVisible()) {
                    Double price = form.getClient().getBalance();
                    viewDetailDialog.getBalanceValueLabel().setText(String.valueOf(PriceUtil.round(price)));
                }
            }
        });
    }

    public void extendTime(double hours) {
        timerUtil.extendTime(hours);
    }

    public static boolean isDialogVisible() {
        return instance.isVisible();
    }

    public static void setDialogVisible(TimerUtil timerUtil) {
        PWLauncher.timerUtil = timerUtil;
        initSystemTray(instance);
        addViewDetailOption();
        isLoggedIn = true;
        setUnlockedIcon();
        setTimer();
        instance.setVisible(true);
    }

    public static void updateBalance(Double balance) {
        form.getClient().setBalance(form.getClient().getBalance() + balance);

        System.out.println("salto extendido :: " + form.getClient().getBalance());
        double timeToExtend = TimerUtil.getTimeAsHours(form.getClient().getBalance(), form.getTariff());
        System.out.println("tiempo extendido :: " + timeToExtend);
        timerUtil.extendTime(timeToExtend);
    }

    public static void main(String[] args) {
        new PWLauncher();
    }

}