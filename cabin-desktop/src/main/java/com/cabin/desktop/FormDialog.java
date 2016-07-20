/*
 * Created by JFormDesigner on Mon May 30 21:52:25 COT 2016
 */

package com.cabin.desktop;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.cabin.entity.Client;
import com.cabin.entity.Computer;
import com.cabin.rest.PrizesRuleRest;
import com.cabin.rest.RentRest;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author jose ruiz
 */
public class FormDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private Client client;
    private Computer computer;
    private Double tariff;

    private JLabel userLabel;
    private JLabel userValueLabel;
    private JLabel label13;
    private JLabel pointsLabel;
    private JLabel pointsValueLabel;
    private JLabel balanceLabel;
    private JLabel balanceValueLabel;
    private JLabel experienceLabel;
    private JLabel experienceValueLabel;
    private JLabel groupLabel;
    private JLabel groupValueLabel;
    private JLabel tariffLabel;
    private JLabel tariffValueLabel;
    private JLabel byHourLabel;
    private JLabel newPointsLabel;
    private JTextField newPointsTextField;
    private JLabel bonificationLabel;
    private JLabel bonificationValueLabel;
    private JButton viewBonus;
    private JLabel rentTypeLabel;
    private JComboBox<Object> rentTypeCombobox;
    private JLabel timeLabel;
    private JTextField hourTextField;
    private JTextField minTextField;
    private JLabel priceLabel;
    private JTextField priceTextField;
    private JButton rentButton;
    private JButton rentAndExchangeButton;

    /**
     * @wbp.parser.constructor
     */
    public FormDialog(Frame owner) {
        super(owner);
        initComponents();
    }

    public FormDialog(Dialog owner, Client client, Computer computer, Double tariff) {
        super(owner);
        System.out.println("in FormDialog");
        this.client = client;
        this.computer = computer;
        this.tariff = tariff;
        initComponents();
    }

    private void initComponents() {
        userLabel = new JLabel();
        userValueLabel = new JLabel();
        label13 = new JLabel();
        pointsLabel = new JLabel();
        pointsValueLabel = new JLabel();
        balanceLabel = new JLabel();
        balanceValueLabel = new JLabel();
        experienceLabel = new JLabel();
        experienceValueLabel = new JLabel();
        groupLabel = new JLabel();
        groupValueLabel = new JLabel();
        tariffLabel = new JLabel();
        tariffValueLabel = new JLabel();
        byHourLabel = new JLabel();
        newPointsLabel = new JLabel();
        newPointsTextField = new JTextField();
        bonificationLabel = new JLabel();
        bonificationValueLabel = new JLabel();
        viewBonus = new JButton();
        rentTypeLabel = new JLabel();
        timeLabel = new JLabel();
        hourTextField = new JTextField();
        minTextField = new JTextField();
        rentTypeCombobox = new JComboBox<Object>();
        priceLabel = new JLabel();
        priceTextField = new JTextField();
        rentButton = new JButton();
        rentAndExchangeButton = new JButton();

        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(new ColumnSpec[] {
        		ColumnSpec.decode("30dlu"),
        		FormSpecs.DEFAULT_COLSPEC,
        		ColumnSpec.decode("50dlu"),
        		ColumnSpec.decode("max(48dlu;default)"),
        		ColumnSpec.decode("19dlu"),
        		ColumnSpec.decode("55dlu"),
        		ColumnSpec.decode("98dlu"),
        		ColumnSpec.decode("max(63dlu;default)"),
        		ColumnSpec.decode("40dlu"),},
        	new RowSpec[] {
        		FormSpecs.DEFAULT_ROWSPEC,
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("25dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("25dlu"),
        		RowSpec.decode("19dlu"),
        		RowSpec.decode("20dlu"),}));

        contentPane.setBackground(new Color(1.0F, 1.0F, 1.0F, 0.0F));
        userLabel.setText("Usuario:");
        userValueLabel.setText(client.getName());
        contentPane.add(userLabel, CC.xy(2, 2));
        contentPane.add(userValueLabel, CC.xywh(3, 2, 4, 1));
        contentPane.add(label13, CC.xy(1, 3));

        pointsLabel.setText("Puntos:");
        pointsValueLabel.setText(String.valueOf(client.getPoints()));
        contentPane.add(pointsLabel, CC.xy(2, 3));
        contentPane.add(pointsValueLabel, CC.xy(3, 3));

        balanceLabel.setText("Saldo:");
        balanceValueLabel.setText(String.valueOf(client.getBalance()));
        contentPane.add(balanceLabel, CC.xy(6, 3));
        contentPane.add(balanceValueLabel, CC.xy(7, 3));

        experienceLabel.setText("Experiencia:");
        experienceValueLabel.setText(String.valueOf(client.getExperience()));
        contentPane.add(experienceLabel, CC.xy(8, 3));
        contentPane.add(experienceValueLabel, CC.xy(9, 3));

        groupLabel.setText("Grupo PC:");
        groupValueLabel.setText(computer.getGroup().getName());
        contentPane.add(groupLabel, CC.xy(2, 5));
        contentPane.add(groupValueLabel, CC.xywh(3, 5, 2, 1));

        tariffLabel.setText("Tarifa:");
        tariffValueLabel.setText(String.valueOf(tariff));
        contentPane.add(tariffLabel, CC.xy(6, 5));
        contentPane.add(tariffValueLabel, CC.xy(7, 5));

        byHourLabel.setText("x Hora");
        contentPane.add(byHourLabel, CC.xy(8, 5));

        newPointsLabel.setText("Puntos:");
        contentPane.add(newPointsLabel, CC.xy(2, 7));
        contentPane.add(newPointsTextField, CC.xywh(3, 7, 2, 1));

        bonificationLabel.setText("Bonificacion:");
        contentPane.add(bonificationLabel, CC.xy(6, 7));
        contentPane.add(bonificationValueLabel, CC.xy(7, 7));

        viewBonus.setText("Ver");
        ImageIcon myImage = new ImageIcon(getClass().getResource("/images/VerBtn.jpg"));
        viewBonus = new JButton(myImage);
        viewBonus.setPreferredSize(new Dimension(50, 25));
        viewBonus.setMinimumSize(new Dimension(50, 25));
        viewBonus.setMaximumSize(new Dimension(50, 25));        
        viewBonus.setAlignmentX(0.5F);
        viewBonus.setOpaque(false);
        viewBonus.setContentAreaFilled(false);
        viewBonus.setBorderPainted(false);
        viewBonus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(viewBonus, CC.xy(8, 7));
        viewBonus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("entra viewBonus");
                int points = Integer.parseInt(newPointsTextField.getText());
                System.out.println("entra viewBonus, points: " + points);
                String balance = new PrizesRuleRest().getBonification(client.getLevel().getId(), points);
                System.out.println("entra viewBonus, balance: " + balance);
                bonificationValueLabel.setText(balance);
            }
        });

        rentTypeLabel.setText("Tipo Alquiler:");
        rentTypeCombobox.addItem("Libre");
        rentTypeCombobox.addItem("Control");
        contentPane.add(rentTypeLabel, CC.xy(2, 9));
        contentPane.add(rentTypeCombobox, CC.xy(3, 9));

        timeLabel.setText("Tiempo (HH:MM):");
        contentPane.add(timeLabel, CC.xy(2, 11));
        contentPane.add(hourTextField, CC.xy(3, 11));
        contentPane.add(minTextField, CC.xy(4, 11));

        priceLabel.setText("Precio:");
        contentPane.add(priceLabel, CC.xy(6, 11));
        contentPane.add(priceTextField, CC.xy(7, 11));

        rentButton.setText("Alquilar");
        myImage = new ImageIcon(getClass().getResource("/images/AlquilarBtn.jpg"));
        rentButton = new JButton(myImage);
        rentButton.setPreferredSize(new Dimension(150, 100));
        rentButton.setMinimumSize(new Dimension(150, 100));
        rentButton.setMaximumSize(new Dimension(150, 100));        
        rentButton.setAlignmentX(0.5F);
        rentButton.setOpaque(false);
        rentButton.setContentAreaFilled(false);
        rentButton.setBorderPainted(false);
        rentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RentRest rentRest = new RentRest();
                int rentHour = Integer.parseInt(hourTextField.getText());
                int rentMin = Integer.parseInt(minTextField.getText());
                if (rentMin > 0) {
                	rentHour += (3/5)*(rentMin);
                }
                rentRest.rentComputer(client.getId(), computer.getId(), String.valueOf(rentHour));
            }
        });
        contentPane.add(rentButton, "2, 13, 3, 1");

        rentAndExchangeButton.setText("Alquilar y Canjear");
        myImage = new ImageIcon(getClass().getResource("/images/AlquilarYCanjearBtn.jpg"));
        rentAndExchangeButton = new JButton(myImage);
        rentAndExchangeButton.setPreferredSize(new Dimension(150, 100));
        rentAndExchangeButton.setMinimumSize(new Dimension(150, 100));
        rentAndExchangeButton.setMaximumSize(new Dimension(150, 100));
        rentAndExchangeButton.setAlignmentX(0.5F);
        rentAndExchangeButton.setOpaque(false);
        rentAndExchangeButton.setContentAreaFilled(false);
        rentAndExchangeButton.setBorderPainted(false);
        rentAndExchangeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rentAndExchangeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RentRest rentRest = new RentRest();
                int rentHour = Integer.parseInt(hourTextField.getText());
                int rentMin = Integer.parseInt(minTextField.getText());
                if (rentMin > 0) {
                	rentHour += (3/5)*(rentMin);
                }
                rentRest.rentComputer(client.getId(), computer.getId(), String.valueOf(rentHour));
            }
        });
        contentPane.add(rentAndExchangeButton, CC.xyw(6, 13, 2));

        pack();
        setLocationRelativeTo(getOwner());

        setVisible(true);
    }

}
