package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.Apartment;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.RequestType;
import com.rentals.rentalmanager.common.SingleHouse;

import java.io.IOException;
import java.time.LocalDate;

public class ClientControls {

    public ClientControls() throws IOException {
    }

    ClientGUI gui = new ClientGUI();
    Client client = new Client(null);

    public void addNew(String id) throws IOException {
        client.connect();
        client.outputStream.writeObject(RequestType.NEW);
        client.outputStream.writeObject(id);

        client.inputStream.readBoolean();
        client.close();
    }

    public boolean idAlreadyExists(String id) throws IOException {
        client.connect();
        client.outputStream.writeObject(RequestType.GET);
        client.outputStream.writeObject(id);

        if(!client.inputStream.readBoolean()) {
            client.close();
            return true;

        } else
            client.close();
        return false;
    }

    public boolean addNewTenant(String id, String name) throws IOException {
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
        }
        close();
        return success;
    }

    public boolean hasTenant(RentalProperty property) {
        int numTenants = property.getTenantNames().length;
        if(!((numTenants) == 0)) {
            return true;
        } else
            return false;
    }

    public String[] getTenants(String id) throws IOException, ClassNotFoundException {
        connect();
        RentalProperty property = getProperty(id);
        String[] tenants = property.getTenantNames();

        return tenants;

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

<<<<<<< Updated upstream
    public void updateProperty(String id) throws IOException, ClassNotFoundException {
        double balance;
=======
    public boolean updateProperty(RentalProperty property) throws IOException, ClassNotFoundException {
>>>>>>> Stashed changes

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

}

