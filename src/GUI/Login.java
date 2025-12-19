package GUI;

import API.Employee;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    Font boldFont = new Font("Segoe UI", Font.BOLD, 16);
    Font regularFont = new Font("Segoe UI", Font.PLAIN, 16);
    private final String iconPath = "Database/Images/login icon.png";

    private final ManagementService service;
    private final JLabel usernameLabel;
    private final JTextField usernameField;
    private final JLabel passwordLabel;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public Login(ManagementService service) {
        this.service = service;

        // FRAME SETTINGS
        setTitle("Είσοδος στο σύστημα");
        setSize(450, 325);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
        setIconImage(icon);

        // PANELS
        JPanel outerPanel = new JPanel(new GridBagLayout());
        //outerPanel.setBorder(BorderFactory.createEmptyBorder(10,100,10,10));
        JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 21));
        outerPanel.add(loginPanel);
        /** Δημιουργούμε ένα πάνελ outerPanel για να τοποθετήσουμε μέσα σε αυτό
         τα labels και fields για τα username και password
         ώστε να μην πιάνουν όλη την οθόνη
         */

        // LOGIN PANEL LABELS + FIELDS
        usernameLabel = new JLabel("Username: ", SwingConstants.CENTER);
        usernameLabel.setFont(boldFont);
        loginPanel.add(usernameLabel);

        usernameField = new JTextField(11);
        usernameField.setFont(regularFont);
        loginPanel.add(usernameField);

        passwordLabel = new JLabel("Password: ", SwingConstants.CENTER);
        passwordLabel.setFont(boldFont);
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField(11);
        passwordLabel.setFont(regularFont);
        loginPanel.add(passwordField);

        // LOGIN BUTTON
        loginButton = new JButton("Σύνδεση");
        styleButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPerform();
            }
        });

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 28, 0)); //padding
        buttonPanel.add(loginButton);
        /** Ομοίως, δημιουργούμε ένα πάνελ buttonPanel για να τοποθετήσουμε μέσα σε αυτό το κουμπί */

        //ADD OUTER PANEL + BUTTON PANEL
        add(outerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        //VISIBILITY
        setVisible(true);
    }

    //METHOD FOR LOGIN LISTENER
    private void loginPerform() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Employee employee = service.getEmployeeManager().login(username, password);

        if (employee != null) {
            JOptionPane.showMessageDialog(this, "Επιτυχής σύνδεση!");
            dispose();
            new MainFrame(service);
        } else {
            JOptionPane.showMessageDialog(this, "Σφάλμα σύνδεσης!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(83, 93, 180));
        button.setForeground(new Color(248, 232, 225));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
