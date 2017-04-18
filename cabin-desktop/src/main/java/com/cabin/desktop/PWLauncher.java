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
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.cabin.common.ConnectionDataValidator;
import com.cabin.common.PriceUtil;
import com.cabin.common.TimerUtil;
import com.cabin.entity.Client;
import com.cabin.entity.FormInformation;
import com.cabin.rest.ClientRest;
import com.cabin.rest.ParameterRest;
import com.cabin.rest.RentRest;

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
    
    public static Map<Long, Long> connectionData;

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
        connectionData = new ParameterRest().getConnectionData();
        logger.info("Connecion data: " + connectionData);
        instance = this;
        viewDetailDialog = null;
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
        	viewDetailDialog = null;
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
            viewDetailDialog = null;
            PWDialog.disposeInstance();
            ConnectionDataValidator.checkDataConnection();
            PWDialog.main(null);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void showNotification(FormInformation form, double totalTime) {
        PWLauncher.form = form;
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        JFrame parent = new JFrame();        
        String change_level = "0";
        if ( Integer.parseInt(form.getClient().getChange_level()) == 1 ){
        	PWLauncher.form.getClient().setChange_level(change_level);        	
        	Client clientAux = new ClientRest().changeLevel(form.getRentId(), change_level);
        	JOptionPane.showMessageDialog(parent, "Felicitaciones has pasado al siguiente nivel: " + clientAux.getLevel().getName());
        }
        String bonus = "0";        
        if ( Integer.parseInt(form.getClient().getBonus()) == 1 ){
        	PWLauncher.form.getClient().setBonus(bonus);        	
        	Long bonusIdBeforeChange = (long) 0 ;
			if ( form.getClient().getId_bonification() != null) 
				bonusIdBeforeChange = form.getClient().getId_bonification();
        	Client clientAux = new ClientRest().changeBonification(form.getRentId(), bonus);
        	Double bonification = new ClientRest().getBonification(bonusIdBeforeChange, clientAux.getId_bonification());
        	PWLauncher.form.getClient().setBalance(form.getClient().getBalance() + bonification);
        	JOptionPane.showMessageDialog(parent, "Felicitaciones has recibido una bonificación de: " + bonification + " soles");
        }
        
        new NotificationDialog(TimerUtil.getHoursAsString(totalTime), PWLauncher.form.getClient().getBalance(), PWLauncher.form.getTariff());
    }

    private static void updateNotificationDialog(String totalTime) {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);        
        new NotificationDialog(totalTime, form.getClient().getBalance(), form.getTariff());
    }

    public static void setTimer() {
        timerUtil.replaceActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.info("action listener launcher, " + timerUtil.getRemainingTime());
                form.updateBalance(timerUtil.getRemainingTime());
                trayIcon.displayMessage("", timerUtil.getRemainingTime(), MessageType.NONE);
                if (timerUtil.isShowNotification()) {
                    if (viewDetailDialog != null) {
                        viewDetailDialog.setVisible(false);
                    }
                    updateNotificationDialog(timerUtil.getRemainingTime());
                } else if (timerUtil.isOver()) {
                    if (viewDetailDialog != null) {
                        viewDetailDialog.dispose();
                    }                    
                    Long minutesUsed = timerUtil.getMinutesUsed();
                    double hoursUsed = minutesUsed / 60.0;
                    double totalHours = round(hoursUsed);
                    //double price = totalHours * form.getTariff();
                    new RentRest().endRentComputer(form.getRentId(), String.valueOf(totalHours), String.valueOf(form.getTariff()));

                    instance.stopComputer();
                } else if (viewDetailDialog != null) {
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
    	JFrame parent = new JFrame();        
        String change_level = "0";
        if ( Integer.parseInt(form.getClient().getChange_level()) == 1 ){
        	form.getClient().setChange_level(change_level);
        	Client clientAux = new ClientRest().changeLevel(form.getRentId(), change_level);
        	form.getClient().setPoints(clientAux.getPoints());
        	form.getClient().setExperience(clientAux.getExperience());
        	form.getClient().setLevel(clientAux.getLevel());
        	JOptionPane.showMessageDialog(parent, "Felicitaciones has pasado al siguiente nivel: " + form.getClient().getLevel().getName());
        }
        
        form.getClient().setBalance(form.getClient().getBalance() + balance);
        
        String bonus = "0";        
        if ( Integer.parseInt(form.getClient().getBonus()) == 1 ){
        	form.getClient().setBonus(bonus);        	
        	Long bonusIdBeforeChange = (long) 0;
        	if ( form.getClient().getId_bonification() != null) 
				bonusIdBeforeChange = form.getClient().getId_bonification();
        	Client clientAux = new ClientRest().changeBonification(form.getRentId(), bonus);
        	Double bonification = new ClientRest().getBonification(bonusIdBeforeChange, clientAux.getId_bonification());
        	form.getClient().setPoints(clientAux.getPoints());
        	form.getClient().setExperience(clientAux.getExperience());       
        	form.getClient().setBalance(form.getClient().getBalance() + bonification);
        	JOptionPane.showMessageDialog(parent, "Felicitaciones has recibido una bonificación de: " + bonification + " soles");
        }

        System.out.println("salto extendido :: " + form.getClient().getBalance());
        double timeToExtend = TimerUtil.getTimeAsHours(form.getClient().getBalance(), form.getTariff());
        System.out.println("tiempo extendido :: " + timeToExtend);
        timerUtil.extendTime(timeToExtend);
    }

    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        double factorValue = value * factor;
        long tmp = Math.round(factorValue);
        return (double) tmp / factor;
    }
    
    public static void main(String[] args) {
        new PWLauncher();
    }
    
    

}