package com.rentals.rentalmanager.common;

import java.time.LocalDate;

public class VacationRental extends RentalProperty {
    // 1 for daily, 2 for monthly, 3 for yearly
    private int paymentType;

    public VacationRental(double balance, double price, String id, String description, LocalDate moveIn) {
        super(balance, price, id, description, moveIn);
        // daily for now.
        this.paymentType = 1;
    }

    @Override
    protected int dueDatesSinceMoveIn() {
        return 0;
    }

    @Override
    protected LocalDate nextDueDate() {
        LocalDate dueDate = this.getMoveInDate();
        // continue incrementing days, months, or years until the due date is after the current date.
        while (this.getMoveInDate().isAfter(LocalDate.now())) {
            switch (this.paymentType) {
                case 1:
                    dueDate = dueDate.plusDays(1);
                case 2:
                    dueDate = dueDate.plusMonths(1);
                case 3:
                    dueDate = dueDate.plusYears(1);
            }
        }
        return dueDate;
    }
}
