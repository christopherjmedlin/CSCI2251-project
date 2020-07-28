package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.RequestType;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame {

    //GUI
    public JPanel mainPanel;
    ClientGUI gui = new ClientGUI();

    //Networking
    private Socket sock;
    private String server;
    public ObjectInputStream inputStream;
    public ObjectOutputStream outputStream;


    public Client(String host) throws IOException {
        super("Client");
        server = host;


        mainPanel = new JPanel();

        ClientGUI guiPanel = new ClientGUI();
        add(guiPanel.getGuiPanel());
        this.setSize(720, 300);

    }

    public void runClient() {
    }

    public void connect() throws IOException {
        // attempts connection
        sock = new Socket(InetAddress.getByName(server), 1234);
        streams();
        System.out.println("Connected to server.");
    }

    private void close() throws IOException {
        outputStream.close();
        inputStream.close();
        sock.close();
        System.out.println("Disconnected from server.");
    }

    private void streams() throws IOException {
        //to server
        outputStream = new ObjectOutputStream(sock.getOutputStream());
        outputStream.flush();

        //from server
        inputStream = new ObjectInputStream(sock.getInputStream());
    }

    private void sendData(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void addNew(String id) throws IOException {
        connect();
        outputStream.writeObject(RequestType.NEW);
        outputStream.writeObject(id);

        inputStream.readBoolean();
        close();
    }

    public boolean newAlreadyExists(String id) throws IOException {
        connect();
        outputStream.writeObject(RequestType.NEW);
        outputStream.writeObject(id);

        if(!inputStream.readBoolean()) {
            close();
            return true;

        } else
            close();
            return false;
    }



}

