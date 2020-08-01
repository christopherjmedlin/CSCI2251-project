package com.rentals.rentalmanager.client;

import com.intellij.uiDesigner.core.GridLayoutManager;
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
<<<<<<< Updated upstream
        this.setSize(720, 300);

    }

    public void connect() throws IOException {
        // attempts connection
        sock = new Socket(InetAddress.getByName(server), 1234);
        streams();
        System.out.println("Connected to server.");
    }

    void close() throws IOException {
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





=======
        this.setSize(815, 300);
    }

>>>>>>> Stashed changes
}

