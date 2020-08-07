package com.rentals.rentalmanager.client;
import com.rentals.rentalmanager.client.Client;

import javax.swing.*;
import java.io.IOException;

public class ClientRun {

    public static void main(String[] args) throws IOException {
        Client app;

        if (args.length == 0) {
            app = new Client("127.0.0.1");
        }
        else {
            app = new Client(args[0]);
        }

        app.setVisible(true);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
