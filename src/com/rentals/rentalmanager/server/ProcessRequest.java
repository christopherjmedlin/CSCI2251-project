package com.rentals.rentalmanager.server;

import com.rentals.rentalmanager.common.PropertySearch;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.RequestType;
import com.rentals.rentalmanager.common.Tenant;
import com.rentals.rentalmanager.server.db.PropertyQueries;
import com.rentals.rentalmanager.server.db.TenantQueries;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The ProcessRequest task reads a request from a socket, processes it, and returns an appropriate response
 */
public class ProcessRequest implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ProcessRequest.class.getName());

    Socket sock;
    ObjectInputStream in;
    ObjectOutputStream out;

    String dbUser;
    String dbPass;

    Properties config;

    /**
     * @param sock reference to the socket which this task will read a request from
     */
    public ProcessRequest(Socket sock, Properties config) {
        this.sock = sock;
        this.config = config;
        this.dbPass = config.getProperty("dbpass", "testing");
        this.dbUser = config.getProperty("dbuser", "testing");
    }

    public void run() {
        try {
            getInputAndOutputStreams();
            RequestType type = (RequestType) in.readObject();
            handle(type);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe(e.toString());
        }
    }

    // sets in and out to the output stream and input stream from the socket
    private void getInputAndOutputStreams() throws IOException {
        this.out = new ObjectOutputStream(this.sock.getOutputStream());
        this.in = new ObjectInputStream(this.sock.getInputStream());
    }

    // based on the request type, passes control to another appropriate method
    private void handle(RequestType type) {
        PropertyQueries properties = new PropertyQueries(this.dbUser, this.dbPass);
        try {
            switch (type) {
                case GET:
                    getRequest(properties);
                    break;
                case NEW:
                    newRequest(properties);
                    break;
                case SEARCH:
                    searchRequest(properties);
                    break;
                case UPDATE:
                    updateRequest(properties);
                    break;
                case DELETE:
                    deleteRequest(properties);
                    break;
                case NEWTENANT:
                    newTenantRequest(properties);
                    break;
                case DELETETENANT:
                    deleteTenantRequest(properties);
                    break;
            }
            // flush the output stream
            this.out.flush();
        } catch (ClassNotFoundException e) {
            try {
                sendError("Could not determine class of object sent.");
            } catch (IOException ioe) {
                LOGGER.severe(ioe.toString());
            }
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
        properties.close();
    }

    // handles a get request from the user for a property
    private void getRequest(PropertyQueries db) throws ClassNotFoundException, IOException {
        LOGGER.info("Processing a GET request for a property.");
        String id = (String) in.readObject();
        out.writeBoolean(true);
        out.writeObject(db.getPropertyById(id));
    }

    // handles a request from the user to create a new property
    private void newRequest(PropertyQueries db) throws ClassNotFoundException, IOException {
        LOGGER.info("Processing a NEW property request.");
        String id = (String) in.readObject();

        char propertyType = id.charAt(0);
        if (propertyType != 'S' && propertyType != 'A' && propertyType != 'V')
            sendError("Property ID must begin with S, A, or V (the property type)");

        try {
            db.newProperty(id);
            out.writeBoolean(true);
        } catch (IllegalArgumentException e) {
            // this is ran if the ID already existed in the database
            LOGGER.info("ID already exists.");
            sendError(e.getMessage());
        }
    }

    // handles a search request from the user
    private void searchRequest(PropertyQueries db) throws ClassNotFoundException, IOException {
        LOGGER.info("Processing a SEARCH request.");
        // read search parameters
        PropertySearch searchParameters = (PropertySearch) in.readObject();
        // perform query
        boolean endOfMonth = Boolean.parseBoolean(this.config.getProperty("endOfMonth", "false"));
        List<String> searchResult = db.search(searchParameters, endOfMonth);
        // return result
        out.writeBoolean(true);
        out.writeObject(searchResult);
    }

    // handles a request to update a property
    private void updateRequest(PropertyQueries db) throws ClassNotFoundException, IOException {
        LOGGER.info("Processing an UPDATE request.");
        // read the argument, a RentalProperty
        RentalProperty property = (RentalProperty) in.readObject();
        // update database
        db.updateProperty(property);
        // indicate success
        out.writeBoolean(true);
    }

    // handles a request to delete a property
    private void deleteRequest(PropertyQueries db) throws ClassNotFoundException, IOException {
        LOGGER.info("Processing a DELETE request.");
        // read ID
        String propertyId = (String) in.readObject();
        // delete
        db.deleteProperty(propertyId);
        // success
        out.writeBoolean(true);
    }

    // handles a request to create a new tenant
    private void newTenantRequest(PropertyQueries db) throws ClassNotFoundException, IOException {
        LOGGER.info("Processing a NEWTENANT request.");
        // read the ID of the property the new tenant will belong to
        String propertyId = (String) in.readObject();
        // read the full name of the tenant
        String name = (String) in.readObject();
        int id = new TenantQueries(db.getConnection()).newTenant(propertyId, name);
        out.writeBoolean(true);
        // return the id of the new tenant created
        out.writeInt(id);
    }

    // handles a request to delete a tenant
    private void deleteTenantRequest(PropertyQueries db) throws IOException {
        LOGGER.info("Processing a DELETETENANT request.");
        // read ID of tenant to be deleted
        int tenantId = in.readInt();
        new TenantQueries(db.getConnection()).deleteTenant(tenantId);
        out.writeBoolean(true);
    }

    private void sendError(String message) throws IOException {
        out.writeBoolean(false);
        out.writeObject(message);
    }
}
