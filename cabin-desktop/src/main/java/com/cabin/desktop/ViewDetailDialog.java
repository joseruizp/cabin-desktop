package com.cabin.desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.cabin.common.TimerUtil;
import com.cabin.entity.Client;
import com.cabin.entity.Failure;
import com.cabin.entity.FormInformation;
import com.cabin.rest.FailureRest;
import com.cabin.rest.PrizesRuleRest;
import com.cabin.rest.RentRest;
import java.awt.SystemColor;
import javax.swing.JComboBox;

public class ViewDetailDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final NumberFormat PRICE_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    private final JPanel contentPanel = new JPanel();
    private JLabel userLabel;
    private JLabel pointsLabel;
    private JLabel balanceLabel;
    private JLabel experienceLabel;
    private JLabel nivelLabel;
    private JTextField newPointsTextField;

    private JLabel balanceValueLabel;

    /**
     * Create the dialog.
     */
    public ViewDetailDialog(final FormInformation form, final TimerUtil timerUtil, final PWLauncher launcher) {
        final ViewDetailDialog thisDialog = this;

        final FailureRest failureRest = new FailureRest();
        final List<Failure> failures = failureRest.getFailures();

        setBounds(100, 100, 500, 297);
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

        balanceValueLabel = new JLabel(PRICE_FORMAT.format(form.getClient().getBalance()));
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

        {
            nivelLabel = new JLabel("Nivel:");
            nivelLabel.setBounds(33, 82, 40, 14);
            contentPanel.add(nivelLabel);
        }
        
        final JLabel nivelValueLabel = new JLabel(String.valueOf(form.getClient().getLevel().getName()));
        nivelValueLabel.setBounds(73, 82, 80, 14);
        contentPanel.add(nivelValueLabel);
        
        JLabel groupLabel = new JLabel("Grupo PC:");
        groupLabel.setBounds(163, 82, 67, 14);
        contentPanel.add(groupLabel);

        JLabel groupValueLabel = new JLabel(form.getComputer().getGroup().getName());
        groupValueLabel.setBounds(230, 82, 80, 14);
        contentPanel.add(groupValueLabel);

        JLabel tariffLabel = new JLabel("Tarifa:");
        tariffLabel.setBounds(320, 82, 42, 14);
        contentPanel.add(tariffLabel);

        JLabel tariffValueLabel = new JLabel(PRICE_FORMAT.format(form.getTariff()));
        tariffValueLabel.setBounds(362, 82, 18, 14);
        contentPanel.add(tariffValueLabel);

        JLabel lblPorHora = new JLabel("soles/hora");
        lblPorHora.setBounds(380, 82, 60, 14);
        contentPanel.add(lblPorHora);

        JLabel lblPuntosACanjear = new JLabel("Puntos a Canjear:");
        lblPuntosACanjear.setBounds(33, 130, 107, 14);
        contentPanel.add(lblPuntosACanjear);


        JLabel bonusLabel = new JLabel("Saldo a Obtener:");
        bonusLabel.setBounds(257, 130, 101, 14);
        contentPanel.add(bonusLabel);        
        

        final JLabel bonusValueLabel = new JLabel("");
        bonusValueLabel.setBounds(354, 130, 49, 14);
        contentPanel.add(bonusValueLabel);

        final JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(SystemColor.inactiveCaptionText);
        messageLabel.setBounds(33, 201, 300, 14);
        contentPanel.add(messageLabel);

        
        newPointsTextField = new JTextField();
        newPointsTextField.setBounds(139, 127, 59, 20);
        contentPanel.add(newPointsTextField);
        newPointsTextField.setColumns(10);
        newPointsTextField.getDocument().addDocumentListener(new DocumentListener() {
        	public void changedUpdate(DocumentEvent e) {
        		getBonus();
        	}
			public void removeUpdate(DocumentEvent e) {
				getBonus();
			}
			public void insertUpdate(DocumentEvent e) {
				getBonus();
			}
			public void getBonus() {
					Integer points = 0;
					if ( !newPointsTextField.getText().isEmpty() ){	
						points = Integer.parseInt(newPointsTextField.getText());
						if ( points > form.getClient().getPoints() ){
							points = form.getClient().getPoints();
							//newPointsTextField.setText(Integer.toString(points));
							SwingUtilities.invokeLater( new ChangeTextFieldPoints(points));
						}
					}
					else{
						bonusValueLabel.setText("");
					}
	                if ( points <= form.getClient().getPoints() ){
	                	if ( points > 0){
	                		String balance = new PrizesRuleRest().getBonification(form.getClient().getLevel().getId(), points);
	                		messageLabel.setText("");
	                		bonusValueLabel.setText(balance);	                		
	                	}
	                	else{
	                		messageLabel.setText("Debe colocar un valor de puntos mayor a 0.");
	                		bonusValueLabel.setText("");
	                	}                	
	                }
	                else{
	            		messageLabel.setText("Los puntos a canjear exceden a los puntos que usted tiene.");
	            		bonusValueLabel.setText("");
	                }
			}
			class ChangeTextFieldPoints implements Runnable {			    
			    private int points;
	
			    ChangeTextFieldPoints(int points) {
			      this.points = points;			      
			    }
	
			    public void run() {			    
			    	newPointsTextField.setText(Integer.toString(points));
			    }
			}
        });
        
        JLabel failureLabel = new JLabel("Tipo de Falla:");
        failureLabel.setBounds(33, 171, 90, 14);
        contentPanel.add(failureLabel);

        final JComboBox<String> failureCombo = new JComboBox<String>();
        failureCombo.setBounds(137, 168, 98, 20);

        failureCombo.addItem("Seleccionar");
        for (Failure failure : failures) {
            failureCombo.addItem(failure.getName());
        }

        contentPanel.add(failureCombo);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnReportarFalla = new JButton("Reportar Falla");
        btnReportarFalla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = failureCombo.getSelectedIndex();
                if (index > 0) {
                    Failure failure = failures.get(index - 1);
                    failureRest.reportFailure(form.getComputer().getId(), failure.getId());
                    messageLabel.setText("Falla del equipo reportada.");
                }
            }
        });
        buttonPane.add(btnReportarFalla);
        
        /*
        JButton viewBtn = new JButton("Ver Bono");
        buttonPane.add(viewBtn);
        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int points = Integer.parseInt(newPointsTextField.getText());
                if ( points <= form.getClient().getPoints() ){
                	if ( points > 0){
                		String balance = new PrizesRuleRest().getBonification(form.getClient().getLevel().getId(), points);
                		bonusValueLabel.setText(balance);
                	}
                	else{
                		messageLabel.setText("Debe colocar un valor de puntos mayor a 0.");
                	}                	
                }
                else
            		messageLabel.setText("Los puntos a canjear exceden a los puntos que usted tiene.");
            }
        });
        */
        
        JButton exchangeBtn = new JButton("Canjear");
        buttonPane.add(exchangeBtn);
        getRootPane().setDefaultButton(exchangeBtn);
        exchangeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Integer points = Integer.parseInt(newPointsTextField.getText());
            	if ( points <= form.getClient().getPoints() ){
            		if ( points > 0){
		                Client client = new RentRest().exchangePoints(form.getRentId(), newPointsTextField.getText());
		                balanceValueLabel.setText(String.valueOf(client.getBalance()));
		                pointsValueLabel.setText(String.valueOf(client.getPoints()));
		                experienceValueLabel.setText(String.valueOf(client.getExperience()));
		                double hours = TimerUtil.getTimeAsHours(client.getBalance(), form.getTariff());
		                launcher.extendTime(hours);
		                messageLabel.setText("Canje de Puntos realizado satisfactoriamente.");
		                newPointsTextField.setText("");
		                bonusValueLabel.setText("");
            		}
            		else
            			messageLabel.setText("Debe colocar un valor de puntos mayor a 0.");
            	}
            	else
            		messageLabel.setText("Los puntos a canjear exceden a los puntos que usted tiene.");
            }
        });

        JButton stopBtn = new JButton("Detener");
        buttonPane.add(stopBtn);
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Long minutesUsed = timerUtil.getMinutesUsed();
                double hoursUsed = minutesUsed / 60.0;
                double totalHours = round(hoursUsed);
                //double price = round(totalHours * form.getTariff());
                new RentRest().endRentComputer(form.getRentId(), String.valueOf(totalHours), String.valueOf(form.getTariff()));

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

    public JLabel getBalanceValueLabel() {
        return this.balanceValueLabel;
    }
}
