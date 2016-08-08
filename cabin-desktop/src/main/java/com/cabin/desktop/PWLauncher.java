package com.cabin.desktop;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import com.cabin.common.TimerUtil;
import com.cabin.entity.FormInformation;

public class PWLauncher extends JDialog implements ActionListener {
    private static final long serialVersionUID = -3759856811214634419L;
    public static PWDialog pwScreen = null;
    private static boolean locked = false;
    private static TrayIcon trayIcon;

    private String totalTime;
    private Long secondsUsed;
    private boolean isLoggedIn;
    private FormInformation form;
    private TimerUtil timerUtil;

    public PWLauncher(String totalTime, Long secondsUsed, FormInformation form) {
        this();
        this.totalTime = totalTime;
        this.secondsUsed = secondsUsed;
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
                new ViewDetailDialog(form, timerUtil, thisDialog);
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
            new ViewDetailDialog(form, timerUtil, this);
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

    public static void showNotification(FormInformation form, double totalTime) {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        new NotificationDialog(getHoursAsString(totalTime), form);
    }

    public static String getHoursAsString(double hours) {
        long hour = (long) hours;
        double fraction = hours - hour;
        String hourString = hour < 10 ? ("0" + hour) : (Long.toString(hour));
        long minutes = Math.round(60 * fraction);
        String minutesString = minutes < 10 ? ("0" + minutes) : (Long.toString(minutes));
        return hourString + ":" + minutesString + ":00";
    }

    public void showTimer() {
        this.timerUtil = new TimerUtil(totalTime, secondsUsed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timerUtil.isOver()) {
                    timerUtil.stop();
                    blockComputer();
                }
                trayIcon.displayMessage("", timerUtil.getRemainingTime(), MessageType.NONE);
            }
        });
    }

    public static void main(String[] args) {
        new PWLauncher();
    }
}