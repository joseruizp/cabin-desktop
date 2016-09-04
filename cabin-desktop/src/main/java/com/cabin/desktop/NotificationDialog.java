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
import com.cabin.entity.FormInformation;

public class NotificationDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    {
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private TimerUtil timerUtil;
    private final FormInformation formInformation;
    private final JLabel availableBalanceValueLabel;

    public NotificationDialog(String totalTime, FormInformation form) {
        this.formInformation = form;
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

        Double price = this.formInformation.getClient().getBalance();
        availableBalanceValueLabel = new JLabel(String.valueOf(PriceUtil.round(price)));
        getContentPane().add(availableBalanceValueLabel);

        JLabel restTimeLabel = new JLabel("Tiempo Restante:");
        getContentPane().add(restTimeLabel);

        JLabel restTimeValueLabel = new JLabel(totalTime);
        getContentPane().add(restTimeValueLabel);
        setTimer(restTimeValueLabel, totalTime);

        final NotificationDialog thisDialog = this;
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("closing notification");
                new PWLauncher(timerUtil, formInformation);
                thisDialog.dispose();
            }
        });
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    
    private void setTimer(final JLabel timerLabel, String totalTime) {
        this.timerUtil = new TimerUtil(totalTime, null, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("set timer notification: " + timerUtil.getRemainingTime());
                formInformation.updateBalance(timerUtil.getRemainingTime());
                Double price = formInformation.getClient().getBalance();
                availableBalanceValueLabel.setText(String.valueOf(PriceUtil.round(price)));
                timerLabel.setText(timerUtil.getRemainingTime());
            }
        });
    }

}
