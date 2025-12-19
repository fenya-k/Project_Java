package GUI;

import API.ManagementService;

import javax.swing.*;

public class AddClientDialog extends JDialog implements StyleAddCancel {

    private final ManagementService service;

    public AddClientDialog(JFrame parent, ManagementService service) {
        super(parent, "Προσθήκη Νέου ", true);
        this.service = service;
    }

}

// like car //