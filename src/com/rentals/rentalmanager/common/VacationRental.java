package com.rentals.rentalmanager.common;

import java.time.LocalDate;

public class VacationRental extends RentalProperty {

    public VacationRental(double balance, double price, String id, String description, LocalDate moveIn) {
        super(balance, price, id, description, moveIn);
    }

        @Override
        protected int dueDatesSinceMoveIn() {
            return 0;
        }

        @Override
        protected boolean dueDateApproaching() {
            return false;
        }
}
