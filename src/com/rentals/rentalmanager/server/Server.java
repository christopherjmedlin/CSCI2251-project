package com.rentals.rentalmanager.server;

import com.rentals.rentalmanager.server.db.DatabaseUtilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

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

        Properties config = getProperties();
        ServerStart server = new ServerStart(config);

        try {
            LOGGER.info("Starting server on port " + config.getProperty("port", "1234") + ".");
            server.run("testing", "testing");
        } catch (IOException e) {
            LOGGER.severe("Could not start server, port is already being used by another application.");
        }
    }

    private static Properties getProperties() {
        Properties config = new Properties();

        try {
            FileInputStream in = new FileInputStream("server.properties");
            config.load(in);
        } catch (FileNotFoundException e) {
            LOGGER.warning("Server properties file not found.");
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }

        return config;
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
