package com.cabin.desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class ViewDetailDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JLabel userLabel;
    private JLabel pointsLabel;
    private JLabel balanceLabel;
    private JLabel experienceLabel;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ViewDetailDialog dialog = new ViewDetailDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ViewDetailDialog() {
        setBounds(100, 100, 489, 245);
        BorderLayout borderLayout = new BorderLayout();
        getContentPane().setLayout(borderLayout);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            userLabel = new JLabel("Usuario:");
            userLabel.setBounds(33, 31, 40, 14);
            contentPanel.add(userLabel);
        }
        
        JLabel userValueLabel = new JLabel("");
        userValueLabel.setBounds(83, 31, 70, 14);
        contentPanel.add(userValueLabel);
        {
            pointsLabel = new JLabel("Puntos:");
            pointsLabel.setBounds(163, 31, 37, 14);
            contentPanel.add(pointsLabel);
        }
        
        JLabel pointsValueLabel = new JLabel("");
        pointsValueLabel.setBounds(210, 31, 37, 14);
        contentPanel.add(pointsValueLabel);
        {
            balanceLabel = new JLabel("Saldo:");
            balanceLabel.setBounds(257, 31, 30, 14);
            contentPanel.add(balanceLabel);
        }
        
        JLabel balanceValueLabel = new JLabel("");
        balanceValueLabel.setBounds(297, 31, 37, 14);
        contentPanel.add(balanceValueLabel);
        {
            experienceLabel = new JLabel("Experiencia:");
            experienceLabel.setBounds(344, 31, 59, 14);
            contentPanel.add(experienceLabel);
        }
        
        JLabel experienceValueLabel = new JLabel("");
        experienceValueLabel.setBounds(404, 31, 37, 14);
        contentPanel.add(experienceValueLabel);
        
        JLabel groupLabel = new JLabel("Grupo PC:");
        groupLabel.setBounds(33, 82, 52, 14);
        contentPanel.add(groupLabel);
        
        JLabel groupValueLabel = new JLabel("");
        groupValueLabel.setBounds(83, 82, 117, 14);
        contentPanel.add(groupValueLabel);
        
        JLabel tariffLabel = new JLabel("Tarifa:");
        tariffLabel.setBounds(257, 82, 32, 14);
        contentPanel.add(tariffLabel);
        
        JLabel tariffValueLabel = new JLabel("");
        tariffValueLabel.setBounds(297, 82, 32, 14);
        contentPanel.add(tariffValueLabel);
        
        JLabel lblPorHora = new JLabel("por Hora");
        lblPorHora.setBounds(335, 82, 42, 14);
        contentPanel.add(lblPorHora);
        
        JLabel lblPuntosACanjear = new JLabel("Puntos a Canjear:");
        lblPuntosACanjear.setBounds(33, 130, 87, 14);
        contentPanel.add(lblPuntosACanjear);
        
        textField = new JTextField();
        textField.setBounds(125, 127, 59, 20);
        contentPanel.add(textField);
        textField.setColumns(10);
        
        JLabel bonusLabel = new JLabel("Bonificacion:");
        bonusLabel.setBounds(257, 130, 61, 14);
        contentPanel.add(bonusLabel);
        
        JLabel bonusValueLabel = new JLabel("");
        bonusValueLabel.setBounds(328, 130, 49, 14);
        contentPanel.add(bonusValueLabel);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            
            JButton viewBtn = new JButton("Ver Bono");
            viewBtn.setActionCommand("OK");
            buttonPane.add(viewBtn);
            {
                JButton exchangeBtn = new JButton("Canjear");
                exchangeBtn.setActionCommand("OK");
                buttonPane.add(exchangeBtn);
                getRootPane().setDefaultButton(exchangeBtn);
            }
            {
                JButton stopBtn = new JButton("Detener");
                stopBtn.setActionCommand("Cancel");
                buttonPane.add(stopBtn);
            }
        }
    }
}
