package com.rentals.rentalmanager.common;

import java.time.LocalDate;
import java.time.Period;

public class VacationRental extends RentalProperty {
    // 1 for daily, 2 for monthly, 3 for yearly
    private int paymentType;

    public VacationRental(double balance, double price, String id, String description, LocalDate moveIn,
                          boolean endOfMonth) {
        super(balance, price, id, description, moveIn, endOfMonth);
        // monthly for now.
        this.paymentType = 2;
    }

    @Override
    protected int dueDatesSinceMoveIn() {
        // initialize to 1 to include the initial payment
        int dueDates = 1;
        Period duration = this.rentalPeriod();

        switch (this.paymentType) {
            case 1:
                dueDates += duration.getDays();
            case 2:
                dueDates += duration.getMonths();
            case 3:
                dueDates += duration.getYears();
        }

        return dueDates;
    }

    @Override
    protected LocalDate nextDueDate() {
        LocalDate dueDate = this.getMoveInDate();
        // continue incrementing days, months, or years until the due date is after the current date.
        LocalDate currentDate = LocalDate.now();
        while (currentDate.isAfter(dueDate)) {
            switch (this.paymentType) {
                case 1:
                    dueDate = dueDate.plusDays(1);
                    break;
                case 2:
                    dueDate = dueDate.plusMonths(1);
                    break;
                case 3:
                    dueDate = dueDate.plusYears(1);
            }
        }
        return dueDate;
    }
}
