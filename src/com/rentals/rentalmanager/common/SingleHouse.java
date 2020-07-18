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
            dueDates += 1;
            // subtract that year from the period
            duration = duration.minusYears(1);
        }
        // add the remaining months
        dueDates += duration.getMonths();

        return dueDates;
    }

    @Override
    protected boolean dueDateApproaching() {
        // assuming that rent is due at the beginning of each month,
        // when there is 7 or less days remaining until rent, return true

        //get current day of month and initialize int
        LocalDate currentDate = LocalDate.now();
        int daysOfMonthPassed = (currentDate.getDayOfMonth());

        // initialize int to days in current month, ignoring leap year value for now
        Month currentMonth = currentDate.getMonth();
        int daysInMonth = currentMonth.length(false);

        if ((daysInMonth - daysOfMonthPassed) <= 7) {
            return true;
        } else
            return false;
    }
}
