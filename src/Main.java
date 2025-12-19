import API.ManagementService;
import GUI.Login;
import GUI.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // LOAD DATA (initialize backend service)
        System.out.println("Loading database...");
        ManagementService service = new ManagementService();
        service.readAllCSV();
        System.out.println("Database loaded.");

        // LAUNCH GUI (in the Swing Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            //new Login(service);
            new MainFrame(service);
        });
    }
}