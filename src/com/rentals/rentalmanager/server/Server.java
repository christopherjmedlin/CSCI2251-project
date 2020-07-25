package com.rentals.rentalmanager.server;

import com.rentals.rentalmanager.server.db.DatabaseUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        initializeLogging();

        try {
            if (!DatabaseUtilities.isDatabaseInitialized("testing", "testing"))
                DatabaseUtilities.initDB("testing", "testing");
        } catch (SQLException e) {
            LOGGER.severe("Could not connect to or initialize database.");
            LOGGER.severe(e.toString());
            System.exit(1);
        } catch (FileNotFoundException e) {
            LOGGER.severe("Attempted to initialize database, but could not find init.sql file");
            System.exit(1);
        }

        // TODO important! port number should be configurable (by command line args perhaps)
        int port = 1234;
        ServerStart server = new ServerStart(port);
        try {
            LOGGER.info("Starting server on port " + port + ".");
            // TODO define db user and password from cmd args!!!!!
            server.run("testing", "testing");
        } catch (IOException e) {
            LOGGER.severe("Could not start server, port is already being used by another application.");
        }
    }

    // registers a file handler to the main logger
    private static void initializeLogging() {
        Logger log = Logger.getLogger("com.rentals.rentalmanager.server");
        try {
            // true is for appending to an existing file instead of erasing
            FileHandler fh = new FileHandler("rentalmanager.log", true);
            fh.setLevel(Level.ALL);
            log.addHandler(fh);
        } catch (IOException e) {
            LOGGER.warning("Log file could not be opened. Information will only be printed to console.");
        }
    }
}
