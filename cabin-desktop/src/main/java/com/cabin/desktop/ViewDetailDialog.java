package com.cabin.desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.cabin.common.TimerUtil;
import com.cabin.entity.Client;
import com.cabin.entity.FormInformation;
import com.cabin.rest.PrizesRuleRest;
import com.cabin.rest.RentRest;
import java.awt.SystemColor;

public class ViewDetailDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final NumberFormat PRICE_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    private final JPanel contentPanel = new JPanel();
    private JLabel userLabel;
    private JLabel pointsLabel;
    private JLabel balanceLabel;
    private JLabel experienceLabel;
    private JTextField newPointsTextField;

    /**
     * Create the dialog.
     */
    public ViewDetailDialog(final FormInformation form, final TimerUtil timerUtil, final PWLauncher launcher) {
        final ViewDetailDialog thisDialog = this;
        setBounds(100, 100, 489, 264);
        BorderLayout borderLayout = new BorderLayout();
        getContentPane().setLayout(borderLayout);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            userLabel = new JLabel("Usuario:");
            userLabel.setBounds(33, 31, 55, 14);
            contentPanel.add(userLabel);
        }

        JLabel userValueLabel = new JLabel(form.getClient().getName());
        userValueLabel.setBounds(83, 31, 70, 14);
        contentPanel.add(userValueLabel);
        {
            pointsLabel = new JLabel("Puntos:");
            pointsLabel.setBounds(163, 31, 47, 14);
            contentPanel.add(pointsLabel);
        }

        final JLabel pointsValueLabel = new JLabel(String.valueOf(form.getClient().getPoints()));
        pointsValueLabel.setBounds(210, 31, 37, 14);
        contentPanel.add(pointsValueLabel);
        {
            balanceLabel = new JLabel("Saldo:");
            balanceLabel.setBounds(257, 31, 40, 14);
            contentPanel.add(balanceLabel);
        }

        final JLabel balanceValueLabel = new JLabel(PRICE_FORMAT.format(form.getClient().getBalance()));
        balanceValueLabel.setBounds(297, 31, 53, 14);
        contentPanel.add(balanceValueLabel);
        {
            experienceLabel = new JLabel("Experiencia:");
            experienceLabel.setBounds(360, 31, 70, 14);
            contentPanel.add(experienceLabel);
        }

        final JLabel experienceValueLabel = new JLabel(String.valueOf(form.getClient().getExperience()));
        experienceValueLabel.setBounds(434, 31, 37, 14);
        contentPanel.add(experienceValueLabel);

        JLabel groupLabel = new JLabel("Grupo PC:");
        groupLabel.setBounds(33, 82, 62, 14);
        contentPanel.add(groupLabel);

        JLabel groupValueLabel = new JLabel(form.getComputer().getGroup().getName());
        groupValueLabel.setBounds(93, 82, 117, 14);
        contentPanel.add(groupValueLabel);

        JLabel tariffLabel = new JLabel("Tarifa:");
        tariffLabel.setBounds(257, 82, 42, 14);
        contentPanel.add(tariffLabel);

        JLabel tariffValueLabel = new JLabel(PRICE_FORMAT.format(form.getTariff()));
        tariffValueLabel.setBounds(297, 82, 32, 14);
        contentPanel.add(tariffValueLabel);

        JLabel lblPorHora = new JLabel("por Hora");
        lblPorHora.setBounds(335, 82, 52, 14);
        contentPanel.add(lblPorHora);

        JLabel lblPuntosACanjear = new JLabel("Puntos a Canjear:");
        lblPuntosACanjear.setBounds(33, 130, 107, 14);
        contentPanel.add(lblPuntosACanjear);

        newPointsTextField = new JTextField();
        newPointsTextField.setBounds(139, 127, 59, 20);
        contentPanel.add(newPointsTextField);
        newPointsTextField.setColumns(10);

        JLabel bonusLabel = new JLabel("Saldo a Obtener:");
        bonusLabel.setBounds(257, 130, 101, 14);
        contentPanel.add(bonusLabel);

        final JLabel bonusValueLabel = new JLabel("");
        bonusValueLabel.setBounds(354, 130, 49, 14);
        contentPanel.add(bonusValueLabel);
        
        final JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(SystemColor.inactiveCaptionText);
        messageLabel.setBounds(33, 168, 264, 14);
        contentPanel.add(messageLabel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton viewBtn = new JButton("Ver Bono");
        buttonPane.add(viewBtn);
        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int points = Integer.parseInt(newPointsTextField.getText());
                String balance = new PrizesRuleRest().getBonification(form.getClient().getLevel().getId(), points);
                bonusValueLabel.setText(balance);
            }
        });

        JButton exchangeBtn = new JButton("Canjear");
        buttonPane.add(exchangeBtn);
        getRootPane().setDefaultButton(exchangeBtn);
        exchangeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Client client = new RentRest().exchangePoints(form.getRentId(), newPointsTextField.getText());
                balanceValueLabel.setText(String.valueOf(client.getBalance()));
                pointsValueLabel.setText(String.valueOf(client.getPoints()));
                experienceValueLabel.setText(String.valueOf(client.getExperience()));
                double hours = TimerUtil.getTimeAsHours(client.getBalance(), form.getTariff());
                launcher.extendTime(hours);
                messageLabel.setText("Canje de Puntos realizado satisfactoriamente.");
            }
        });

        JButton stopBtn = new JButton("Detener");
        buttonPane.add(stopBtn);
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Long secondsUsed = timerUtil.getSecondsUsed();
                Long minutesUsed = new Long(secondsUsed / 60);
                double hoursUsed = minutesUsed / 60.0;
                double totalHours = round(hoursUsed);
                double price = totalHours * form.getTariff();
                new RentRest().endRentComputer(form.getRentId(), String.valueOf(totalHours), String.valueOf(price));

                thisDialog.dispose();
                launcher.stopComputer();
            }

            private double round(double value) {
                long factor = (long) Math.pow(10, 2);
                double factorValue = value * factor;
                long tmp = Math.round(factorValue);
                return (double) tmp / factor;
            }
        });

        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setVisible(true);
    }
}
