package com.rentals.rentalmanager.server.db;

import com.rentals.rentalmanager.common.Apartment;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.SingleHouse;
import com.rentals.rentalmanager.common.VacationRental;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Used for retrieving, updating, and creating RentalProperties in the database
 */
public class PropertyQueries {
    private static final Logger LOGGER = Logger.getLogger(PropertyQueries.class.getName());

    private static final String URL = "jdbc:derby:properties";
    private String username;
    private String password;

    private Connection db;
    private PreparedStatement allProperties;
    private PreparedStatement newProperty;
    private PreparedStatement updateProperty;

    public PropertyQueries() {
        try {
            this.db = DriverManager.getConnection(URL);

            // TODO i am assuming rows are ordered by the primary key by default, but we should make sure of this
            // returns every property in the db
            this.allProperties = this.db.prepareStatement(
                    "SELECT * FROM properties"
            );

            // updates every field in a property, with the last argument being the id of it
            this.updateProperty = this.db.prepareStatement(
                    "UPDATE properties " +
                       "SET balance=? price=? moveIn=? description=? " +
                       "WHERE id=?"
            );

            // create new property with user-defined id, with everything else empty
            this.newProperty = this.db.prepareStatement(
                    "INSERT INTO properties " +
                       "(id) VALUES (?)"
            );
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }
    }

    public List<RentalProperty> getAllProperties() {
        List<RentalProperty> properties = new ArrayList<>();

        LOGGER.info("Querying all properties.");
        try (ResultSet results = this.allProperties.executeQuery()) {

            while (results.next()) {
                String id = results.getString("id");
                int balance = results.getInt("balance");
                int price = results.getInt("price");
                LocalDate moveIn = results.getDate("moveIn").toLocalDate();
                String description = results.getString("description");

                // construct different subclasses of RentalProperty based on the first character of the id
                switch (id.charAt(0)) {
                    case 'A':
                        properties.add(new Apartment(balance, price, id, description, moveIn));
                    case 'S':
                        properties.add(new SingleHouse(balance, price, id, description, moveIn));
                    case 'V':
                        properties.add(new VacationRental(balance, price, id, description, moveIn));
                }
            }
            return properties;
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }

        return properties;
    }

    public int newProperty(String id) {
        LOGGER.info("Adding new property to database with id " + id + ".");
        try {
            newProperty.setString(1, id);
            return newProperty.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }

        return 0;
    }

    public void close() {
        try {
            LOGGER.info("Closing database connection.");
            this.db.close();
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }
    }
}
