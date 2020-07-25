package com.rentals.rentalmanager.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame {

    //GUI
    public JPanel mainPanel;


    //Networking
    private Socket client;
    private String server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public Client(String host) {
        super("Client");
        server = host;


        mainPanel = new JPanel();

        ClientGUI guiPanel = new ClientGUI();
        add(guiPanel.getGuiPanel());
        this.pack();


    }

    public void runClient() {
    }

    public void connect() throws IOException {
        // attempts connection
        client = new Socket(InetAddress.getByName(server), 1234);
        streams();
    }

    private void close() throws IOException {
        outputStream.close();
        inputStream.close();
        client.close();
    }

    private void streams() throws IOException {
        //to server
        outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.flush();

        //from server
        inputStream = new ObjectInputStream(client.getInputStream());
    }

    private void sendData(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}

