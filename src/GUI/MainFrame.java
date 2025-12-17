package GUI;

import API.ManagementService;

import javax.swing.*;
import java.awt.*;
public class MainFrame extends JFrame {
    private final ManagementService service;

    public MainFrame(ManagementService service) {
        this.service = service;

        //frame settings
        setTitle("Car Rental System");//title
        setSize(800, 600);//size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//program stops when we close it
        setLocationRelativeTo(null);//frame will be in the centre
        setLayout(new BorderLayout());//

        //start screen
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());//buttons will be in the centre
        startPanel.setBackground(new Color(240, 240, 240));//color-to be changed

        //client button
        JButton clientButton = new JButton("Client View(Cars available)");
        styleButton(clientButton);

        //employee button
        JButton employeeButton = new JButton("Employee Login");
        styleButton(employeeButton);

        //buttons to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);//insets between buttons-to be changed
        gbc.gridx = 0;//selects the first column
        gbc.gridy = 0;//selects the first row
        startPanel.add(clientButton, gbc);//adds clientButton to the panel using position above

        gbc.gridy = 1;//changes setting to the second row
        startPanel.add(employeeButton, gbc);//adds employeeButton to the panel below the first button

        //add panel to the window
        add(startPanel, BorderLayout.CENTER);

        clientButton.addActionListener(e -> {
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
    }
    //void for buttons
    private  void styleButton(JButton button){
        button.setFont(new Font("Arial",Font.BOLD,16));
        button.setPreferredSize(new Dimension(250,40));
        button.setFocusPainted(false);



    }



}
