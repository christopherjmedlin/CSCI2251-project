package com.rentals.rentalmanager.server.db;

import com.rentals.rentalmanager.common.Apartment;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.SingleHouse;
import com.rentals.rentalmanager.common.VacationRental;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class DatabaseUtilities {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtilities.class.getName());
    public static final String URL = "jdbc:derby:properties;create=true";

    /**
     * Checks if the init.sql script has been ran in the database. If the database doesn't exist at all, a new one will
     * be created (but the tables will remain uninitialized), and false will be returned
     */
    public static boolean isDatabaseInitialized(String username, String password) throws SQLException {
        Connection db = DriverManager.getConnection(DatabaseUtilities.URL, username, password);
        DatabaseMetaData dbm = db.getMetaData();
        // check if properties table is there
        ResultSet tables = dbm.getTables(null, null, "PROPERTIES", null);
        // return whether there is any table in the result set
        boolean initialized = tables.next();
        db.close();
        return initialized;
    }

    /**
     * Runs init.sql in the database (assumes it is the in the working directory)
     */
    public static void initDB(String username, String password) throws SQLException, FileNotFoundException {
        Connection db = DriverManager.getConnection(DatabaseUtilities.URL, username, password);
        Scanner in = new Scanner(new File("init.sql"));

        StringBuilder statement = new StringBuilder();
        while (in.hasNext()) {
            String line = in.nextLine();
            if (!line.startsWith("--")) {
                statement.append(line);
            }
            // if it is the end of an operation
            if (line.contains(";")) {
                // get rid of the semicolon i guess?
                statement.deleteCharAt(statement.length()-1);
                Statement st = db.createStatement();
                // run it
                st.execute(statement.toString());
                // clear the string builder
                statement.delete(0, statement.length());
            }
            else {
                statement.append('\n');
            }
        }
        db.close();
    }

    /**
     * Returns the ids of every property in the given list whose payment status matches the one given.
     */
    public static List<String> filterByRentalStatus(List<RentalProperty> properties, int rentalStatus) {
        int i = 0;
        List<String> ids = new ArrayList<>();
        for (RentalProperty p : properties) {
            // 0 corresponds to the n/a option in the GUI, so it shouldn't filter at all.
            if (p.paymentStatus() == rentalStatus || rentalStatus == 0)
                ids.add(p.getId());
        }
        return ids;
    }

    /**
     * If the result set given contains a property, this will transfer the data from its columns into a RentalProperty
     * object with the appropriate subclass based on the first character of its ID, and then return that.
     */
    public static RentalProperty getPropertyFromResultSet(ResultSet results, boolean endOfMonth) throws SQLException {
        String id = results.getString("id");
        int balance = results.getInt("balance");
        int price = results.getInt("price");
        Date moveInTemp = results.getDate("moveIn");
        // important to not call toLocalDate if the moveIn is null, else we will have null pointer exception
        LocalDate moveIn = moveInTemp == null ? null : moveInTemp.toLocalDate();
        String description = results.getString("description");

        // construct different subclasses of RentalProperty based on the first character of the id
        switch (id.charAt(0)) {
            case 'A':
                return new Apartment(balance, price, id, description, moveIn, endOfMonth);
            case 'S':
                return new SingleHouse(balance, price, id, description, moveIn, endOfMonth);
            case 'V':
                return new VacationRental(balance, price, id, description, moveIn, endOfMonth);
        }

        return null;
    }
}
