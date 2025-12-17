package GUI;

import API.Employee;
import API.EmployeeManager;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final ManagementService service;

    public MainFrame(ManagementService service) {
        this.service = service;

        //frame settings
        setTitle("Car Rental System");//title
        setSize(600, 400);//size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//program stops when we close it
        setLocationRelativeTo(null);//frame will be in the centre
        setLayout(new BorderLayout());//

        //start screen
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());//buttons will be in the centre
        startPanel.setBackground(new Color(240, 240, 240));//color-to be changed

        //login button
        JButton loginButton = new JButton("Employee Login");
        styleButton(loginButton);

        //add panel to the window
        startPanel.add(loginButton);
        add(startPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            //creating panel
            JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 10));

            JTextField userField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            loginPanel.add(new JLabel("Username:"));
            loginPanel.add(userField);
            loginPanel.add(new JLabel("Password:"));
            loginPanel.add(passwordField);

            //dialog with this panel
            int res = JOptionPane.showConfirmDialog(
                    this,
                    loginPanel,
                    "System login",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                    );

            if (res == JOptionPane.OK_OPTION) {
                String username = userField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                EmployeeManager empManager = service.getEmployeeManager();

                Employee foundEmployee = empManager.findByUsername(username);

                if (foundEmployee == null) {
                    //error:there is no user with this username
                    JOptionPane.showMessageDialog(this,
                            "User '" + username + "' not found",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    //user exists,checking for password
                    if (foundEmployee.getPassword().equals(password)) {
                        //success: password is correct
                        JOptionPane.showMessageDialog(this, "Successful Login! Welcome, " + foundEmployee.getName());
                        //opening admin menu
                        new AdminMenuFrame(service, foundEmployee).setVisible(true);
                        //closing window(for login)
                        dispose();
                    } else {
                        //error: wrong password
                        JOptionPane.showMessageDialog(this, "Wrong password for user '" + username + "'", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
    }

    //void for buttons
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(250, 40));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.GRAY);
    }
}

    /* //client button
        JButton clientButton = new JButton("Client View(Cars available)");
        styleButton(clientButton);

        //buttons to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);//insets between buttons-to be changed
        gbc.gridx = 0;//selects the first column
        gbc.gridy = 0; //selects the first row
        //startPanel.add(clientButton, gbc);//adds clientButton to the panel using position above

        gbc.gridy = 1;//changes setting to the second row
        startPanel.add(employeeButton, gbc);//adds employeeButton to the panel below the first button


        /*clientButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Car Table will be shown here");
        });

            employeeButton.addActionListener(e -> {
                        //login window to be added
                        String username = JOptionPane.showInputDialog(this, "Enter Username:");
                        if (username != null && !username.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Trying to login as: " + username);
                        }
                    }
            );
        }*/

