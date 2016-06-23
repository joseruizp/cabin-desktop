/*
 * Created by JFormDesigner on Mon May 30 21:52:25 COT 2016
 */

package com.cabin.desktop;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

/**
 * @author jose ruiz
 */
public class FormDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private Client client;
	private Computer computer;
	
	public FormDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public FormDialog(Dialog owner, Client client, Computer computer) {
		super(owner);
		System.out.println("in FormDialog");
		this.client = client;
		this.computer = computer;
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - jose ruiz
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
		bonificationValueLabel = new JTextField();
		viewBonus = new JButton();
		rentTypeLabel = new JLabel();
		timeLabel = new JLabel();
		timeTextField = new JTextField();
		rentTypeCombobox = new JComboBox();
		priceLabel = new JLabel();
		priceTextField = new JTextField();
		rentButton = new JButton();
		rentAndExchangeButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"30dlu, default, 50dlu, default, 15dlu, 50dlu, 69dlu:grow, default, 40dlu",
			"default, 9*(19dlu), 20dlu"));

		//---- label1 ----
		userLabel.setText("Usuario:");
		userValueLabel.setText(client.getName());
		contentPane.add(userLabel, CC.xy(2, 2));
		contentPane.add(userValueLabel, CC.xywh(3, 2, 4, 1));
		contentPane.add(label13, CC.xy(1, 3));

		//---- label2 ----
		pointsLabel.setText("Puntos:");
		pointsValueLabel.setText(String.valueOf(client.getPoints()));
		contentPane.add(pointsLabel, CC.xy(2, 3));
		contentPane.add(pointsValueLabel, CC.xy(3, 3));

		//---- label3 ----
		balanceLabel.setText("Saldo:");
		balanceValueLabel.setText(String.valueOf(client.getBalance()));
		contentPane.add(balanceLabel, CC.xy(6, 3));
		contentPane.add(balanceValueLabel, CC.xy(7, 3));

		//---- label4 ----
		experienceLabel.setText("Experiencia:");
		experienceValueLabel.setText(String.valueOf(client.getExperience()));
		contentPane.add(experienceLabel, CC.xy(8, 3));
		contentPane.add(experienceValueLabel, CC.xy(9, 3));

		//---- label5 ----
		groupLabel.setText("Grupo PC:");
		groupValueLabel.setText(computer.getGroup().getName());
		contentPane.add(groupLabel, CC.xy(2, 5));
		contentPane.add(groupValueLabel, CC.xywh(3, 5, 2, 1));

		//---- label6 ----
		tariffLabel.setText("Tarifa:");
		contentPane.add(tariffLabel, CC.xy(6, 5));
		contentPane.add(tariffValueLabel, CC.xy(7, 5));

		//---- label7 ----
		byHourLabel.setText("x Hora");
		contentPane.add(byHourLabel, CC.xy(8, 5));

		//---- label8 ----
		newPointsLabel.setText("Puntos:");
		contentPane.add(newPointsLabel, CC.xy(2, 7));
		contentPane.add(newPointsTextField, CC.xywh(3, 7, 2, 1));

		//---- label9 ----
		bonificationLabel.setText("Bonificacion:");
		contentPane.add(bonificationLabel, CC.xy(6, 7));
		contentPane.add(bonificationValueLabel, CC.xy(7, 7));
		
		viewBonus.setText("Ver");
		contentPane.add(viewBonus, CC.xy(9, 7));
		viewBonus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int points = Integer.parseInt(newPointsTextField.getText());
				Double balance = new PrizesRuleRest().getBonification(client.getLevel().getId(), points);
				bonificationValueLabel.setText(String.valueOf(balance));
			}
		});

		//---- label11 ----
		rentTypeLabel.setText("Tipo Alquiler:");
		contentPane.add(rentTypeLabel, CC.xy(2, 9));
		contentPane.add(rentTypeCombobox, CC.xy(3, 9));

		//---- label11 ----
		timeLabel.setText("Tiempo:");
		contentPane.add(timeLabel, CC.xy(2, 11));
		contentPane.add(timeTextField, CC.xy(3, 11));

		//---- label12 ----
		priceLabel.setText("Precio:");
		contentPane.add(priceLabel, CC.xy(6, 11));
		contentPane.add(priceTextField, CC.xy(7, 11));

		//---- button1 ----
		rentButton.setText("Alquilar");
		rentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RentRest rentRest = new RentRest();
				String rentTime = timeTextField.getText();
				rentRest.rentComputer(client.getId(), computer.getId(), rentTime);
			}
		});
		contentPane.add(rentButton, CC.xy(4, 13));
		
		rentAndExchangeButton.setText("Alquilar y Canjear");
		rentAndExchangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RentRest rentRest = new RentRest();
				String rentTime = timeTextField.getText();
				rentRest.rentComputer(client.getId(), computer.getId(), rentTime);
			}
		});
		contentPane.add(rentAndExchangeButton, CC.xy(6, 13));

		//---- button2 ----
		cancelButton.setText("Cancelar");
		contentPane.add(cancelButton, CC.xy(8, 13));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		setVisible(true);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - jose ruiz
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
	private JTextField bonificationValueLabel;
	private JButton viewBonus;
	private JLabel rentTypeLabel;
	private JComboBox rentTypeCombobox;
	private JLabel timeLabel;
	private JTextField timeTextField;
	private JLabel priceLabel;
	private JTextField priceTextField;
	private JButton rentButton;
	private JButton rentAndExchangeButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

}
