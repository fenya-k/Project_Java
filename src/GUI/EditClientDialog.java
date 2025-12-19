package GUI;

import API.Car;
import API.Client;
import API.ManagementService;

import javax.swing.*;

public class EditClientDialog extends JDialog implements StyleAddCancel {
    private final ManagementService service;
    private Client client;

    public EditClientDialog(TableCar parent, ManagementService service, Client client) {
        super(parent, "Επεξεργασία Οχήματος", true);
        this.service = service;
        this.client = client;

    }
}

// like car //
