package GUI;

import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final String iconPath = "Database/Images/login icon.png";

    private final ManagementService service;

    public MainFrame(ManagementService service) {
        this.service = service;

        // FRAME
        setTitle("Σύστημα Ενοικιάσεων Αυτοκινήτων");                 //τίτλος
        setSize(900, 600);                             //μέγεθος
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            //κλείσιμο στην έξοδο
        setLocationRelativeTo(null);                              //κεντράρισμα
        setLayout(new BorderLayout());
        Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
        setIconImage(icon);

        // MAIN PAGE PANEL
        JPanel mainPage = new JPanel();
        mainPage.setLayout(new BoxLayout(mainPage, BoxLayout.Y_AXIS));
        mainPage.setBorder(BorderFactory.createEmptyBorder(50, 10, 20, 10));

        JLabel welcomeLabel = new JLabel("Καλωσήρθατε!");
        Font font = new Font("Segoe UI", Font.BOLD | Font.ITALIC, 25);
        welcomeLabel.setFont(font);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        Image scaledIcon = icon.getScaledInstance(256, 160, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPage.add(welcomeLabel);
        mainPage.add(iconLabel);
        add(mainPage);

        // MENU BAR
        JMenuBar menuBar = new JMenuBar();

        // CAR //
        JMenu carsMenu = new JMenu("Οχήματα");
        JMenuItem addCar = new JMenuItem("Προσθήκη Οχήματος");
        JMenuItem listCars = new JMenuItem("Λίστα Οχημάτων");
        carsMenu.add(addCar);
        carsMenu.add(listCars);
        // ADD CAR DIALOG //
        addCar.addActionListener(e -> {
            AddCarDialog dialog = new AddCarDialog(this, service);
            dialog.setVisible(true);
        });
        // CAR TABLE //
        listCars.addActionListener(e -> {
            TableCar dialog = new TableCar(this, service);
            dialog.setVisible(true);
        });

        // CLIENT //
        JMenu clientsMenu = new JMenu("Πελάτες");
        JMenuItem addClient = new JMenuItem("Προσθήκη Πελάτη");
        JMenuItem listClients = new JMenuItem("Λίστα Πελατών");
        clientsMenu.add(addClient);
        clientsMenu.add(listClients);

        // RENTALS //
        JMenu rentalsMenu = new JMenu("Ενοικιάσεις");
        JMenuItem addRental = new JMenuItem("Προσθήκη Ενοικίασης");
        JMenuItem removeRental = new JMenuItem("Επιστροφή Οχήματος");
        JMenuItem listRental = new JMenuItem("Λίστα Ενοικιάσεων");
        rentalsMenu.add(addRental);
        rentalsMenu.add(removeRental);
        rentalsMenu.addSeparator();
        rentalsMenu.add(listRental);

        // EMPLOYEES //
        JMenu employeesMenu = new JMenu("Υπάλληλοι");
        JMenuItem addEmployee = new JMenuItem("Προσθήκη Υπάλληλου");
        JMenuItem listEmployee = new JMenuItem("Λίστα Υπαλλήλων");
        employeesMenu.add(addEmployee);
        employeesMenu.add(listEmployee);

        // LOGOUT //
        JButton logoutButton = new JButton("Αποσύνδεση");
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setForeground(new Color(8, 46, 131));
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // LOGOUT ACTION LISTENER //
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Θέλετε σίγουρα να αποσυνδεθείτε;",
                    "Αποσύνδεση",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // Κλείνει το MainFrame
                new Login(service); // Ανοίγει το Login
            }
        });

        // ADD TO MENU //
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(carsMenu);
        menuBar.add(clientsMenu);
        menuBar.add(rentalsMenu);
        menuBar.add(employeesMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(logoutButton);
        menuBar.add(Box.createHorizontalStrut(10));

        // SET MENU //
        setJMenuBar(menuBar);


        setVisible(true);
    }
}