package com.rentals.rentalmanager.tests;

import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.SingleHouse;

import java.time.LocalDate;

/**
 * Tests the due date functionality of rental properties
 */
public class RentalPropertyTest {
    public static void main(String[] args) {
        RentalProperty p1 = new SingleHouse(500,500, "SABQ555", "",
                LocalDate.now().minusYears(1).plusDays(5));
        // right now this property should have a due date approaching
        assert p1.paymentStatus() == 1;
        p1.setMoveIn(p1.getMoveInDate().minusDays(10));
        // now it is past due
        assert p1.paymentStatus() == 2;
        p1.setBalance(1000);
        assert p1.paymentStatus() == 0;
        p1.setMoveIn(p1.getMoveInDate().minusMonths(1));
        assert p1.paymentStatus() == 2;
    }
}
