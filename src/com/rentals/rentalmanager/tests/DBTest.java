package com.rentals.rentalmanager.tests;

import com.rentals.rentalmanager.common.*;
import com.rentals.rentalmanager.server.db.PropertyQueries;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Tests the methods offered by PropertyQueries.
 */
public class DBTest {
    private static Logger LOGGER = Logger.getLogger("testing");

    public static void main(String[] args) {
        PropertyQueries queries = new PropertyQueries(args[0], args[1]);
        testNewProperty(queries);
        testGetAllProperties(queries);
        testGetPropertyById(queries);
        testUpdateProperty(queries);
        testSearch(queries);
    }

    // upon running this the second time, exceptions will be logged by PropertyQueries regarding
    // integrity. these can be ignored.
    private static void testNewProperty(PropertyQueries queries) {
        try {
            queries.newProperty("VABQ123");
            queries.newProperty("AABQ12-321");
            queries.newProperty("SABQ452");
        } catch (IllegalArgumentException ignored) {
            LOGGER.info("test properties already made");
        }
        LOGGER.info("testNewProperty passed.");
    }

    private static void testGetAllProperties(PropertyQueries queries) {
        List<String> properties = queries.getAllPropertyIds();
        assert properties.size() >= 3;
        assert properties.get(0).equals("AABQ12-321");
        assert properties.get(1).equals("SABQ452");
        assert properties.get(2).equals("VABQ123");
        LOGGER.info("testGetAllProperties passed.");
    }

    private static void testGetPropertyById(PropertyQueries queries) {
        RentalProperty p = queries.getPropertyById("AABQ12-321");
        assert p.getId().equals("AABQ12-321");
        assert queries.getPropertyById("non-existent id!") == null;
        LOGGER.info("testGetPropertyById passed.");
    }

    private static void testUpdateProperty(PropertyQueries queries) {
        RentalProperty p = new SingleHouse(100.00, 2000.00, "VABQ123", "it's a house",
                LocalDate.of(1, 2, 3));
        queries.updateProperty(p);
        RentalProperty updatedProperty = queries.getPropertyById("VABQ123");
        assert updatedProperty.getBalance() == 100.00;
        assert updatedProperty.getPrice() == 2000.00;
        assert updatedProperty.getDescription().equals("it's a house");
        assert updatedProperty.getMoveIn().getDayOfMonth() == 3;
        LOGGER.info("testUpdateProperty passed.");
    }

    private static void testSearch(PropertyQueries queries) {
        // test searching by id
        List<String> l = queries.search(new PropertySearch("AABQ12-321", 0, true));
        assert l.size() == 1;
        assert l.get(0).equals("AABQ12-321");

        LOGGER.info("testSearch passed.");
    }
}
