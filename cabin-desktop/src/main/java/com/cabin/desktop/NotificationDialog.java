package com.cabin.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.swing.JDialog;
import javax.swing.JLabel;

import com.cabin.common.PriceUtil;
import com.cabin.common.TimerUtil;

public class NotificationDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    {
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private static TimerUtil timerUtil;
    private static String totalTime;
    private static Double balance;
    private static Double tariff;

    private static JLabel availableTimeValueLabel;
    private static JLabel availableBalanceValueLabel;

    private static NotificationDialog instance;

    public NotificationDialog(String totalTime, Double balance, Double tariff) {
        NotificationDialog.totalTime = totalTime;
        NotificationDialog.balance = balance;
        NotificationDialog.tariff = tariff;
        setTitle("Notificaciones");
        setResizable(false);
        setBounds(100, 100, 728, 94);
        getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

        JLabel availableTimeLabel = new JLabel("Tiempo Disponible:");
        getContentPane().add(availableTimeLabel);

        availableTimeValueLabel = new JLabel(totalTime);
        getContentPane().add(availableTimeValueLabel);

        JLabel availableBalanceLabel = new JLabel("Saldo Disponible:");
        getContentPane().add(availableBalanceLabel);

        availableBalanceValueLabel = new JLabel(String.valueOf(PriceUtil.round(balance)));
        getContentPane().add(availableBalanceValueLabel);

        setTimer(totalTime);

        final NotificationDialog thisDialog = this;
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("closing notification");
                // new PWLauncher(timerUtil, formInformation);
                cleanStaticVariable();
                PWLauncher.setDialogVisible(timerUtil);
                thisDialog.dispose();
            }
        });
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        instance = this;
    }

    private void cleanStaticVariable() {
        availableTimeValueLabel = null;
        availableBalanceValueLabel = null;
        totalTime = null;
        balance = null;
        tariff = null;
        instance = null;
    }

    private void setTimer(String totalTime) {
        NotificationDialog.timerUtil = new TimerUtil(totalTime, null, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (NotificationDialog.timerUtil != null) {
                    System.out.println("set timer notification: " + NotificationDialog.timerUtil.getRemainingTime());
                    NotificationDialog.balance = TimerUtil.getBalance(NotificationDialog.timerUtil.getRemainingTime(), NotificationDialog.tariff);
                    availableBalanceValueLabel.setText(String.valueOf(PriceUtil.round(NotificationDialog.balance)));
                }
            }
        });
    }

    public static boolean isDialogVisible() {
        if (instance == null) {
            return false;
        }
        return instance.isVisible();
    }

    public static void updateBalance(Double balance) {
        NotificationDialog.balance += balance;
        availableBalanceValueLabel.setText(String.valueOf(PriceUtil.round(NotificationDialog.balance)));

        System.out.println("salto extendido :: " + NotificationDialog.balance);
        double timeToExtend = TimerUtil.getTimeAsHours(NotificationDialog.balance, NotificationDialog.tariff);
        System.out.println("tiempo extendido :: " + timeToExtend);
        NotificationDialog.timerUtil.extendTime(timeToExtend);

        double totalTime = TimerUtil.getHours(NotificationDialog.totalTime);
        String extendedTime = TimerUtil.getHoursAsString(totalTime + timeToExtend);
        System.out.println("time extended as string: " + extendedTime);
        availableTimeValueLabel.setText(extendedTime);
    }

}
