package com.rentals.rentalmanager.common;

import java.time.Period;
import java.time.LocalDate;
import java.time.Month;

public class SingleHouse extends RentalProperty {

    public SingleHouse(double balance, double price, String id, String description, LocalDate moveIn) {
        super(balance, price, id, description, moveIn);

        if (price < 0.0) {
            throw new IllegalArgumentException("Price must be greater than 0.0");
        }

    }

    @Override
    protected int dueDatesSinceMoveIn() {
        // SingleHouse starts yearly, then monthly.
        // initialize to 1 to include the initial payment
        int dueDates = 1;
        Period duration = this.rentalPeriod();

        // if it has at least been a year
        if (duration.getYears() >= 1) {
            // subtract that year from the period
            duration = duration.minusYears(1);
            dueDates++;
        } else {
            return dueDates;
        }
        // add the remaining months
        dueDates += duration.getMonths();

        return dueDates;
    }

    @Override
    protected LocalDate nextDueDate() {
        // begin by adding a year
        LocalDate dueDate = this.getMoveInDate().plusYears(1);
        // continue incrementing months until the dueDate is past the current date
        LocalDate currentDate = LocalDate.now();
        while (currentDate.isAfter(dueDate)) {
            dueDate = dueDate.plusMonths(1);
        }
        return dueDate;
    }
}
