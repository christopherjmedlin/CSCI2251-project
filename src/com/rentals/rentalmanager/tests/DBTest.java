package com.rentals.rentalmanager.tests;

import com.rentals.rentalmanager.common.Apartment;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.SingleHouse;
import com.rentals.rentalmanager.common.VacationRental;
import com.rentals.rentalmanager.server.db.PropertyQueries;

import java.util.List;
import java.util.logging.Logger;

public class DBTest {
    public static void main(String[] args) {
        PropertyQueries queries = new PropertyQueries(args[0], args[1]);
        queries.newProperty("VABQ123");
        queries.newProperty("AABQ12-321");
        queries.newProperty("SABQ452");

        List<RentalProperty> properties = queries.getAllProperties();
        assert properties.get(0) instanceof Apartment;
        assert properties.get(1) instanceof SingleHouse;
        assert properties.get(2) instanceof VacationRental;

        Logger.getLogger("").info("Test passed");
    }
}
