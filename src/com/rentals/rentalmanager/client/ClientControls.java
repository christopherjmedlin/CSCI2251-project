package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.Apartment;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.RequestType;
import com.rentals.rentalmanager.common.SingleHouse;

import java.io.IOException;
import java.time.LocalDate;

public class ClientControls {
    String error;
    ClientGUI gui;
    Client client = new Client(null);

    public ClientControls(ClientGUI gui) throws IOException {
        this.gui = gui;
    }

    /**
     * Makes a NEW request to the server to create a property.
     * @param id the id of the new property
     * @return the success indicator sent by the server
     */
    public boolean addNew(String id) throws IOException {
        client.connect();
        client.outputStream.writeObject(RequestType.NEW);
        client.outputStream.writeObject(id);

        boolean success = client.inputStream.readBoolean();
        if (!success) {
            try {
                // read the error message
                this.error = (String) client.inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        client.close();
        return success;
    }

    public void deleteProperty(String id) throws IOException {
        client.connect();
        client.outputStream.writeObject(RequestType.DELETE);
        client.outputStream.writeObject(id);
        client.inputStream.readBoolean();
        client.close();
    }

    public RentalProperty getProperty(String id) throws IOException, ClassNotFoundException {
        client.connect();
        client.outputStream.writeObject(RequestType.GET);
        client.outputStream.writeObject(id);
        client.inputStream.readBoolean();
        RentalProperty property = (RentalProperty) client.inputStream.readObject();
        return property;
    }

    public void updateProperty(String id) throws IOException, ClassNotFoundException {
        double balance;

        client.connect();
        client.outputStream.writeObject(RequestType.UPDATE);

        if(id.charAt(0) == 'S') {
            balance = Double.parseDouble(gui.balanceField.getText());
            RentalProperty s = new SingleHouse(balance, 100.00, id,
                    "single house", LocalDate.now());
            client.outputStream.writeObject(s);
            client.inputStream.readBoolean();
            client.close();

        } else if(id.charAt(0) == 'A') {
            balance = Double.parseDouble(gui.balanceField.getText());
            RentalProperty a = new Apartment(balance, 100.00, id,
                    "single house", LocalDate.now());
            client.outputStream.writeObject(a);
            client.inputStream.readBoolean();
            client.close();

        } else if(id.charAt(0) == 'V') {
            balance = Double.parseDouble(gui.balanceField.getText());
            RentalProperty v = new Apartment(balance, 100.00, id,
                    "single house", LocalDate.now());
            client.outputStream.writeObject(v);
            client.inputStream.readBoolean();
            client.close();
        } else
            System.out.println("Invalid property type.");
            client.close();

    }

    // if a server communication went wrong, this method will return the message.
    public String getErrorMessage() {
        return this.error;
    }
}

