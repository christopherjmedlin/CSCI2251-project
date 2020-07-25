package com.rentals.rentalmanager.tests;
import com.rentals.rentalmanager.client.Client;
import com.rentals.rentalmanager.client.ClientControl;

import javax.swing.*;

public class ClientTest {

    public static void main(String[] args) {
        Client app;
        ClientControl cc = new ClientControl();

        if (args.length == 0) {
            app = new Client("127.0.0.1");
        }
        else {
            app = new Client(args[0]);
        }
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cc.InitialQuery();

    }
}
