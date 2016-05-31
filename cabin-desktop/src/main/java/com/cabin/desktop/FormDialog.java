/*
 * Created by JFormDesigner on Mon May 30 21:52:25 COT 2016
 */

package com.cabin.desktop;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author jose ruiz
 */
public class FormDialog extends JDialog {
	public FormDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public FormDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - jose ruiz
		label1 = new JLabel();
		label17 = new JLabel();
		label13 = new JLabel();
		label2 = new JLabel();
		label14 = new JLabel();
		label3 = new JLabel();
		label15 = new JLabel();
		label4 = new JLabel();
		label16 = new JLabel();
		label5 = new JLabel();
		textField1 = new JTextField();
		label6 = new JLabel();
		textField2 = new JTextField();
		label7 = new JLabel();
		label8 = new JLabel();
		textField3 = new JTextField();
		label9 = new JLabel();
		textField6 = new JTextField();
		label10 = new JLabel();
		label11 = new JLabel();
		textField4 = new JTextField();
		comboBox1 = new JComboBox();
		label12 = new JLabel();
		textField5 = new JTextField();
		button1 = new JButton();
		button2 = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"30dlu, default, 50dlu, default, 15dlu, 50dlu, 69dlu:grow, default, 40dlu",
			"default, 9*(19dlu), 20dlu"));

		//---- label1 ----
		label1.setText("Usuario:");
		contentPane.add(label1, CC.xy(2, 2));
		contentPane.add(label17, CC.xywh(3, 2, 4, 1));
		contentPane.add(label13, CC.xy(1, 3));

		//---- label2 ----
		label2.setText("Puntos:");
		contentPane.add(label2, CC.xy(2, 3));
		contentPane.add(label14, CC.xy(3, 3));

		//---- label3 ----
		label3.setText("Saldo:");
		contentPane.add(label3, CC.xy(6, 3));
		contentPane.add(label15, CC.xy(7, 3));

		//---- label4 ----
		label4.setText("Experiencia:");
		contentPane.add(label4, CC.xy(8, 3));
		contentPane.add(label16, CC.xy(9, 3));

		//---- label5 ----
		label5.setText("Grupo PC:");
		contentPane.add(label5, CC.xy(2, 5));
		contentPane.add(textField1, CC.xywh(3, 5, 2, 1));

		//---- label6 ----
		label6.setText("Tarifa:");
		contentPane.add(label6, CC.xy(6, 5));
		contentPane.add(textField2, CC.xy(7, 5));

		//---- label7 ----
		label7.setText("x Hora");
		contentPane.add(label7, CC.xy(8, 5));

		//---- label8 ----
		label8.setText("Puntos:");
		contentPane.add(label8, CC.xy(2, 7));
		contentPane.add(textField3, CC.xywh(3, 7, 2, 1));

		//---- label9 ----
		label9.setText("Bonificacion:");
		contentPane.add(label9, CC.xy(6, 7));
		contentPane.add(textField6, CC.xy(7, 7));

		//---- label10 ----
		label10.setText("Canjear");
		contentPane.add(label10, CC.xy(8, 7));

		//---- label11 ----
		label11.setText("Tipo Alquiler:");
		contentPane.add(label11, CC.xy(2, 9));
		contentPane.add(textField4, CC.xy(3, 9));
		contentPane.add(comboBox1, CC.xy(4, 9));

		//---- label12 ----
		label12.setText("Precio:");
		contentPane.add(label12, CC.xy(6, 9));
		contentPane.add(textField5, CC.xy(7, 9));

		//---- button1 ----
		button1.setText("Alquilar");
		contentPane.add(button1, CC.xy(4, 11));

		//---- button2 ----
		button2.setText("Cancelar");
		contentPane.add(button2, CC.xy(6, 11));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - jose ruiz
	private JLabel label1;
	private JLabel label17;
	private JLabel label13;
	private JLabel label2;
	private JLabel label14;
	private JLabel label3;
	private JLabel label15;
	private JLabel label4;
	private JLabel label16;
	private JLabel label5;
	private JTextField textField1;
	private JLabel label6;
	private JTextField textField2;
	private JLabel label7;
	private JLabel label8;
	private JTextField textField3;
	private JLabel label9;
	private JTextField textField6;
	private JLabel label10;
	private JLabel label11;
	private JTextField textField4;
	private JComboBox comboBox1;
	private JLabel label12;
	private JTextField textField5;
	private JButton button1;
	private JButton button2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
