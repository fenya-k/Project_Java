import API.ManagementService;
import API.Rental;
import GUI.Login;
import GUI.MainFrame;

import javax.swing.*;
import java.util.ArrayList;

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
           new Login(service);

        });
    }
}
/*
1,ΙΚΥ1234,1234567000,1/1/2026,5/1/2026,mjones
2,ΝΒΡ5678,1234567800,1/1/2026,6/1/2026,tbrown
3,ΡΤΛ9012,1234567890,1/1/2026,7/1/2026,awhite
4,ΧΖΑ3456,1234567890,1/1/2026,8/1/2026,tbrown
 */