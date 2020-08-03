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
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
                case MAIL:
                    mailRequest();
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
        if (propertyType != 'S' && propertyType != 'A' && propertyType != 'V') {
            sendError("Property ID must begin with S, A, or V (the property type)");
            return;
        }

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
        new TenantQueries(db.getConnection()).newTenant(propertyId, name);
        out.writeBoolean(true);
    }

    // handles a request to delete a tenant
    private void deleteTenantRequest(PropertyQueries db) throws IOException {
        LOGGER.info("Processing a DELETETENANT request.");
        // read ID of tenant to be deleted
        int tenantId = in.readInt();
        new TenantQueries(db.getConnection()).deleteTenant(tenantId);
        out.writeBoolean(true);
    }

    private void mailRequest() throws IOException, ClassNotFoundException {
        LOGGER.info("Processing MAIL request.");

        if (!Boolean.parseBoolean(config.getProperty("mailEnabled"))) {
            sendError("Error sending mail: Mail must be enabled on the server in the server.properties file.");
            LOGGER.warning("Mail not enabled.");
            return;
        }

        String username = config.getProperty("from").split("@")[0];
        System.out.println(username);
        Session session = Session.getDefaultInstance(config, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, config.getProperty("emailPass"));
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(config.getProperty("from"));
            // read addresses and add as recipients to the message
            for (String addr : (List<String>) in.readObject()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
            }
            message.setSubject("Billing Statement");
            message.setContent((String) in.readObject(), "text/html");

            LOGGER.info("Sending mail.");
            Transport.send(message);
            out.writeBoolean(true);
        } catch (MessagingException e) {
            LOGGER.severe(e.toString());
            sendError("Error sending mail: " + e.toString());
        }
    }

    private void sendError(String message) throws IOException {
        out.writeBoolean(false);
        out.writeObject(message);
    }
}
