package com.rentals.rentalmanager.common;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.*;

public class VacationRental extends RentalProperty {
    // can be daily, monthly or yearly
    private ChronoUnit unit;

    public VacationRental(double balance, double price, String id, String description, LocalDate moveIn,
                          boolean endOfMonth) {
        super(balance, price, id, description, moveIn, endOfMonth);
        // monthly for now.
        this.unit = MONTHS;
    }

    @Override
    protected int dueDatesSinceMoveIn() {
        // initialize to 1 to include the initial payment
        int dueDates = 1;
        dueDates += unit.between(this.getMoveInDate(), LocalDate.now());
        return dueDates;
    }

    @Override
    public LocalDate nextDueDate() {
        LocalDate dueDate = this.getMoveInDate();
        // continue incrementing days, months, or years until the due date is after the current date.
        LocalDate currentDate = LocalDate.now();
        while (currentDate.isAfter(dueDate)) {
            switch (this.unit) {
                case DAYS:
                    dueDate = dueDate.plusDays(1);
                    break;
                case MONTHS:
                    dueDate = dueDate.plusMonths(1);
                    break;
                case YEARS:
                    dueDate = dueDate.plusYears(1);
            }
        }
        return dueDate;
    }
}
