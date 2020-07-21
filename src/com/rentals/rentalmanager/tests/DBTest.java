package com.rentals.rentalmanager.tests;

import com.rentals.rentalmanager.common.Apartment;
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.SingleHouse;
import com.rentals.rentalmanager.common.VacationRental;
import com.rentals.rentalmanager.server.db.PropertyQueries;

import java.util.List;
import java.util.logging.Logger;

/**
 * Tests the methods offered by PropertyQueries.
 */
public class DBTest {
    public static void main(String[] args) {
        PropertyQueries queries = new PropertyQueries(args[0], args[1]);
        testNewProperty(queries);
        testGetAllProperties(queries);
        testGetPropertyById(queries);
    }

    // upon running this the second time, exceptions will be logged by PropertyQueries regarding
    // integrity. these can be ignored.
    private static void testNewProperty(PropertyQueries queries) {
        queries.newProperty("VABQ123");
        queries.newProperty("AABQ12-321");
        queries.newProperty("SABQ452");
        Logger.getLogger("testing").info("testNewProperty passed.");
    }

    private static void testGetAllProperties(PropertyQueries queries) {
        List<RentalProperty> properties = queries.getAllProperties();
        assert properties.size() == 3;
        assert properties.get(0) instanceof Apartment;
        assert properties.get(1) instanceof SingleHouse;
        assert properties.get(2) instanceof VacationRental;
        Logger.getLogger("testing").info("testGetAllProperties passed.");
    }

    private static void testGetPropertyById(PropertyQueries queries) {
        RentalProperty p = queries.getPropertyById("AABQ12-321");
        assert p.getId().equals("AABQ12-321");
        assert queries.getPropertyById("non-existent id!") == null;
        Logger.getLogger("testing").info("testGetPropertyById passed.");
    }
}
