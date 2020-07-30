package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.RequestType;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;

public class Client extends JFrame {

    //GUI
    public JPanel mainPanel;

    //Networking
    private Socket sock;
    private String server;
    public ObjectInputStream inputStream;
    public ObjectOutputStream outputStream;


    public Client(String host) throws IOException {
        super("Client");
        server = host;


        mainPanel = new JPanel();

        ClientGUI guiPanel = new ClientGUI(host);
        add(guiPanel.getGuiPanel());
        this.setSize(720, 300);
    }
}

