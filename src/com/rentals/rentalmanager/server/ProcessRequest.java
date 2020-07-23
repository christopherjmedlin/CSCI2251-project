package com.rentals.rentalmanager.server;

import com.rentals.rentalmanager.common.RequestType;
import com.rentals.rentalmanager.server.db.PropertyQueries;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    /**
     * @param sock reference to the socket which this task will read a request from
     */
    public ProcessRequest(Socket sock, String dbUser, String dbPass) {
        this.sock = sock;
        this.dbPass = dbPass;
        this.dbUser = dbUser;
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
    private void handle(RequestType type) throws IOException {
        PropertyQueries db = new PropertyQueries(this.dbUser, this.dbPass);
        try {
            switch (type) {
                case GET:
                    getRequest(db);
                    break;
                case NEW:
                    newRequest(db);
                    break;
            }
            // flush the output stream
            this.out.flush();
        } catch (ClassNotFoundException e) {
            try {
                sendError("Could not determine class of object sent");
            } catch (IOException ioe) {
                LOGGER.severe(ioe.toString());
            }
        }
        db.close();
    }

    // handles a get request from the user for a property
    private void getRequest(PropertyQueries db) throws ClassNotFoundException {
        LOGGER.info("Processing a GET request for a property.");
        try {
            String id = (String) in.readObject();
            out.writeBoolean(true);
            out.writeObject(db.getPropertyById(id));
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
    }

    private void newRequest(PropertyQueries db) throws ClassNotFoundException {
        LOGGER.info("Processing a NEW property request.");
        try {
            String id = (String) in.readObject();
            try {
                db.newProperty(id);
                out.writeBoolean(true);
            } catch (IllegalArgumentException e) {
                // this is if the ID already existed in the database
                LOGGER.info("ID already exists.");
                sendError(e.getMessage());
            }
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
    }

    private void sendError(String message) throws IOException {
        out.writeBoolean(false);
        out.writeObject(message);
    }
}
