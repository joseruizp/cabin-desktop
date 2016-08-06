package com.cabin.desktop;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.cabin.common.PropertiesLoader;
import com.cabin.entity.Client;
import com.cabin.entity.Computer;
import com.cabin.entity.FormInformation;
import com.cabin.rest.ComputerRest;
import com.cabin.rest.LoginRest;
import com.cabin.rest.RentRest;
import com.cabin.rest.TariffRest;

public class PWDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String pw = "123456";
    private PlaceholderPasswordField pwField;
    private PlaceholderTextField txField;
    private JButton loginBtn;
    private JPanel mainPanel;
    private JPanel pwMainPanel;
    private ImagePanel imagePanel;
    private WindowsSecurity security;
    static final String PWMAINPANEL = "Card with Password Field";
    static final String IMAGEPANEL = "Card with Images";
    public static Dimension SCREENDIM;
    public static boolean accepting;
    public static boolean denying;
    public static PWDialog instance;
    private PropertiesLoader propertiesLoader;
    private static final String PROPERTIES_FILE_NAME = "datos.properties";

    public PWDialog() {
        accepting = false;
        denying = false;
        SCREENDIM = getToolkit().getScreenSize();
        propertiesLoader = new PropertiesLoader(PROPERTIES_FILE_NAME);

        JPanel pwPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                try {
                    Image img = ImageIO.read(getClass().getResource("/images/login-background.jpg"));
                    g.drawImage(img, 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        pwPanel.setSize(new Dimension(555, 591));
        pwPanel.setLayout(new BoxLayout(pwPanel, 1));

        pwField = new PlaceholderPasswordField(10);
        pwField.setPlaceholder("Password");
        pwField.setAlignmentX(0.5F);
        pwField.setMaximumSize(new Dimension(319, 55));

        txField = new PlaceholderTextField(10);
        txField.setPlaceholder("Email");
        txField.setAlignmentX(0.5F);
        txField.setMaximumSize(new Dimension(319, 55));

        ImageIcon myImage = new ImageIcon(getClass().getResource("/images/login-button.jpg"));
        loginBtn = new JButton(myImage);
        loginBtn.setAlignmentX(0.5F);
        loginBtn.setOpaque(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(this);

        pwPanel.add(Box.createRigidArea(new Dimension(555, 222)));
        pwPanel.add(txField);
        pwPanel.add(Box.createRigidArea(new Dimension(555, 21)));
        pwPanel.add(pwField);
        pwPanel.add(Box.createRigidArea(new Dimension(555, 21)));
        pwPanel.add(loginBtn);
        pwPanel.add(Box.createRigidArea(new Dimension(555, 200)));

        pwMainPanel = new JPanel();
        pwMainPanel.setBackground(new Color(1.0F, 1.0F, 1.0F, 0.0F));
        pwMainPanel.setLayout(new BoxLayout(pwMainPanel, 1));
        pwMainPanel.add(Box.createRigidArea(new Dimension(getToolkit().getScreenSize().width, getToolkit().getScreenSize().height / 2 - 300)));
        pwMainPanel.add(pwPanel);

        imagePanel = new ImagePanel("/images/Locked.jpg", new Dimension(259, 180));
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent me) {
            }

            public void mouseMoved(MouseEvent me) {
                if ((PWDialog.denying) || (PWDialog.accepting)) {
                    return;
                }
                int x = me.getLocationOnScreen().x;
                int y = me.getLocationOnScreen().y;
                Point p = imagePanel.getPoint();
                Dimension d = imagePanel.getDims();

                if ((x >= p.x) && (x <= p.x + d.width) && (y >= p.y) && (y <= p.y + d.height))
                    showPanel("Card with Password Field");
                else
                    showPanel("Card with Images");
            }
        });
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(imagePanel, "Card with Images");
        mainPanel.add(pwMainPanel, "Card with Password Field");

        setContentPane(mainPanel);
        setPreferredSize(getToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setFocusable(true);
        setResizable(false);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setBackground(new Color(0.1F, 0.1F, 0.1F, 0.25F));
        pack();
        setVisible(true);
        requestFocus();

        security = new WindowsSecurity(this);
    }

    public boolean checkPassword(String in) {
        return in.equals(pw);
    }

    public void clearPassword() {
        pwField.setText(null);
    }

    public void accept(final Client client) {
        accepting = true;

        imagePanel.setImage("/images/Unlocked.jpg", new Dimension(259, 180));
        showPanel("Card with Images");

        // final Dialog thisDialog = this;
        final Computer computer = new ComputerRest().getComputer(propertiesLoader.getLong("id_equipo"));
        final Long headquarterId = propertiesLoader.getLong("id_sede");
        final Double tariff = new TariffRest().getTariff(computer.getGroup().getId(), headquarterId);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                double rentTime = rent(client.getId(), computer.getId(), client.getBalance(), tariff);
                PWLauncher.showNotification(new FormInformation(client, computer, tariff), rentTime);
                security.stop();
            }
        }, 900L);
    }

    private double rent(long clientId, long computerId, double balance, double tariff) {
        RentRest rentRest = new RentRest();
        double rentTime = getTimeToRent(balance, tariff);
        rentRest.rentComputer(clientId, computerId, String.valueOf(rentTime), String.valueOf(balance), null);
        this.dispose();
        return rentTime;
    }

    private double getTimeToRent(double balance, double tariff) {
        return round(balance / tariff);
    }

    private double round(double value) {
        long factor = (long) Math.pow(10, 2);
        double factorValue = value * factor;
        long tmp = Math.round(factorValue);
        return (double) tmp / factor;
    }

    public void displayImage(String path, Dimension dims, Point point) {
        try {
            Image i = ImageIO.read(getClass().getResource(path));
            getGraphics().drawImage(i, point.x, point.y, dims.width, dims.height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deny() {
        denying = true;
        imagePanel.setImage("/images/Locked.jpg", new Dimension(259, 180));
        showPanel("Card with Images");

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                clearPassword();
                showPanel("Card with Password Field");
                PWDialog.denying = false;
            }
        }, 2000L);
    }

    public void showPanel(String title) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, title);
    }

    @SuppressWarnings("deprecation")
    public void actionPerformed(ActionEvent e) {
        String email = txField.getText();
        String password = pwField.getText();

        LoginRest loginRest = new LoginRest();
        Client client = loginRest.login(email, password);

        if (client != null) {
            System.out.println("loggin successfull");
            accept(client);
        } else {
            System.out.println("login failed");
            deny();
        }
    }

    public static void main(String[] args) throws AWTException {
        System.out.println("in PWDialog");
        instance = new PWDialog();
    }
}