package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class ClientControls {
    String error;
    ClientGUI gui;
    String server;

    Socket sock;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    public ClientControls(ClientGUI gui, String server) throws IOException {
        this.server = server;
        this.gui = gui;
        error = "";
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

    /**
     * Makes a NEW request to the server to create a property.
     * @param id the id of the new property
     * @return the success indicator sent by the server
     */
    public boolean addNew(String id) throws IOException {
        connect();
        outputStream.writeObject(RequestType.NEW);
        outputStream.writeObject(id);

        boolean success = inputStream.readBoolean();
        if (!success) {
            try {
                // read the error message
                this.error = (String) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        close();
        return success;
    }

    public void deleteProperty(String id) throws IOException {
        connect();
        outputStream.writeObject(RequestType.DELETE);
        outputStream.writeObject(id);
        inputStream.readBoolean();
        close();
    }

    public RentalProperty getProperty(String id) throws IOException, ClassNotFoundException {
        connect();
        outputStream.writeObject(RequestType.GET);
        outputStream.writeObject(id);
        inputStream.readBoolean();
        RentalProperty property = (RentalProperty) inputStream.readObject();
        close();
        return property;
    }

    public boolean updateProperty(RentalProperty property) throws IOException, ClassNotFoundException {
        double balance;

        connect();
        outputStream.writeObject(RequestType.UPDATE);
        outputStream.writeObject(property);
        boolean success = inputStream.readBoolean();
        close();
        return success;
    }

    public int addNewTenant(String id, String name) throws IOException {
        connect();
        outputStream.writeObject(RequestType.NEWTENANT);

        outputStream.writeObject(id);
        outputStream.writeObject(name);

        boolean success = inputStream.readBoolean();
        if(!success) {
            try {
                // read the error message
                this.error = (String) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // return the id of the new tenant
            return inputStream.readInt();
        }
        close();
        return 0;
    }

    public List<String> search(PropertySearch s) throws IOException, ClassNotFoundException {
        connect();
        outputStream.writeObject(RequestType.SEARCH);
        outputStream.writeObject(s);
        List<String> ids = inputStream.readBoolean() ? (List<String>) inputStream.readObject() : null;
        close();
        return ids;
    }

    public boolean deleteTenant(int id) throws IOException {
        connect();
        outputStream.writeObject(RequestType.DELETETENANT);
        outputStream.writeInt(id);
        outputStream.flush();
        boolean success = inputStream.readBoolean();
        close();
        return success;
    }

    public boolean sendMail(List<String> addresses, String message) throws IOException {
        connect();
        outputStream.writeObject(RequestType.MAIL);
        outputStream.writeObject(addresses);
        outputStream.writeObject(message);
        boolean success = inputStream.readBoolean();
        if(!success) {
            try {
                // read the error message
                this.error = (String) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        close();
        return success;
    }

    // if a server communication went wrong, this method will return the message.
    public String getErrorMessage() {
        return this.error;
    }
}

