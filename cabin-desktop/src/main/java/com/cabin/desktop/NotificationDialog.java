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
import javax.swing.Timer;

import com.cabin.common.TimerUtil;

public class NotificationDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    {
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private TimerUtil timerUtil;
    private Timer timer;

    public NotificationDialog(String totalTime, Double price) {
        setTitle("Notifiaciones");
        setResizable(false);
        setBounds(100, 100, 728, 94);
        getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

        JLabel avalidableTimeLabel = new JLabel("Tiempo Disponible:");
        getContentPane().add(avalidableTimeLabel);

        JLabel availableTimeValueLabel = new JLabel(totalTime);
        getContentPane().add(availableTimeValueLabel);

        JLabel availableBalanceLabel = new JLabel("Saldo Disponible:");
        getContentPane().add(availableBalanceLabel);

        JLabel availableBalanceValueLabel = new JLabel(String.valueOf(round(price)));
        getContentPane().add(availableBalanceValueLabel);

        JLabel restTimeLabel = new JLabel("Tiempo Restante:");
        getContentPane().add(restTimeLabel);

        JLabel restTimeValueLabel = new JLabel("");
        getContentPane().add(restTimeValueLabel);
        setTimer(restTimeValueLabel, totalTime);

        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("closing notification");
                PWLauncher launcher = new PWLauncher(timerUtil.getRemainingTime());
                launcher.showTimer();
                timer.stop();
                System.exit(0);
            }
        });
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    
    private double round(double value) {
        long factor = (long) Math.pow(10, 2);
        double factorValue = value * factor;
        long tmp = Math.round(factorValue);
        return (double) tmp / factor;
    }

    private void setTimer(final JLabel timerLabel, String totalTime) {
        this.timerUtil = new TimerUtil(totalTime);
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timerLabel.setText(timerUtil.getRemainingTime());
            }
        });
        timer.start();
    }

}
