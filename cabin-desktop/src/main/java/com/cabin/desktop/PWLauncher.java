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
import javax.swing.Timer;

import com.cabin.common.TimerUtil;

public class PWLauncher extends JDialog implements ActionListener {
    private static final long serialVersionUID = -3759856811214634419L;
    public static PWDialog pwScreen = null;
    private static boolean locked = false;
    private static TrayIcon trayIcon;

    private String totalTime;
    private boolean isLoggedIn;

    public PWLauncher(String totalTime) {
        this();
        this.totalTime = totalTime;
        this.isLoggedIn = true;
        addViewDetailOption();
        toggleIcon();
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
        trayIcon.setToolTip("Screen locker - UNLOCKED");
        trayIcon.addActionListener(this);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void addViewDetailOption() {
        MenuItem exitItem = new MenuItem("Ver Detalle");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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
            trayIcon.setToolTip("Screen locker - LOCKED");
        } else {
            path = "images/UnlockedIcon.png";
            trayIcon.setToolTip("Screen locker - UNLOCKED");
        }
        trayIcon.setImage(createImage(path));
    }

    public void actionPerformed(ActionEvent arg0) {
        toggleIcon();
        String[] s = null;
        if (isLoggedIn) {

        } else {
            try {
                PWDialog.main(s);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showNotification(double totalTime, double price) {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        new NotificationDialog(getHoursAsString(totalTime), price);
    }

    private static String getHoursAsString(double hours) {
        long hour = (long) hours;
        double fraction = hours - hour;
        String hourString = hour < 10 ? ("0" + hour) : (Long.toString(hour));
        long minutes = Math.round(60 * fraction);
        String minutesString = minutes < 10 ? ("0" + minutes) : (Long.toString(minutes));
        return hourString + ":" + minutesString + ":00";
    }

    public void showTimer() {
        final TimerUtil timerUtil = new TimerUtil(totalTime);
        final ActionListener timerAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trayIcon.displayMessage("", timerUtil.getRemainingTime(), MessageType.NONE);
            }
        };

        Timer timer = new Timer(1000, timerAction);
        timer.start();
    }

    public static void main(String[] args) {
        new PWLauncher();
    }
}