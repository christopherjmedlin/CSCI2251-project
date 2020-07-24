package com.rentals.rentalmanager.server.db;

import com.rentals.rentalmanager.common.Apartment;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.SingleHouse;
import com.rentals.rentalmanager.common.VacationRental;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtilities {
    public static final String URL = "jdbc:derby:properties";

    /**
     * Returns the ids of every property in the given list whose payment status matches the one given.
     */
    public static List<String> filterByRentalStatus(List<RentalProperty> properties, int rentalStatus) {
        int i = 0;
        List<String> ids = new ArrayList<>();
        for (RentalProperty p : properties) {
            if (p.paymentStatus() == rentalStatus)
                ids.add(p.getId());
        }
        return ids;
    }

    /**
     * If the result set given contains a property, this will transfer the data from its columns into a RentalProperty
     * object with the appropriate subclass based on the first character of its ID, and then return that.
     */
    public static RentalProperty getPropertyFromResultSet(ResultSet results) throws SQLException {
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
                return new Apartment(balance, price, id, description, moveIn);
            case 'S':
                return new SingleHouse(balance, price, id, description, moveIn);
            case 'V':
                return new VacationRental(balance, price, id, description, moveIn);
        }

        return null;
    }
}
