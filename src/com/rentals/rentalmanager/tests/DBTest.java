package com.rentals.rentalmanager.tests;

import com.rentals.rentalmanager.common.*;
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
            Logger.getLogger("testing").info("test properties already made");
        }
        Logger.getLogger("testing").info("testNewProperty passed.");
    }

    private static void testGetAllProperties(PropertyQueries queries) {
        List<String> properties = queries.getAllPropertyIds();
        assert properties.size() >= 3;
        assert properties.get(0).equals("AABQ12-321");
        assert properties.get(1).equals("SABQ452");
        assert properties.get(2).equals("VABQ123");
        Logger.getLogger("testing").info("testGetAllProperties passed.");
    }

    private static void testGetPropertyById(PropertyQueries queries) {
        RentalProperty p = queries.getPropertyById("AABQ12-321");
        assert p.getId().equals("AABQ12-321");
        assert queries.getPropertyById("non-existent id!") == null;
        Logger.getLogger("testing").info("testGetPropertyById passed.");
    }

    private static void testSearch(PropertyQueries queries) {
        // test searching by id
        List<String> l = queries.search(new PropertySearch("AABQ12-321", 0, true));
        assert l.size() == 1;
        assert l.get(0).equals("AABQ12-321");
    }
}
