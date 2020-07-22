package com.rentals.rentalmanager.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public abstract class RentalProperty implements Serializable {
    private double balance;
    private double price;
    private String id;
    private String description;
    private LocalDate moveIn;

    public RentalProperty(double balance, double price, String id, String description, LocalDate moveIn) {
        this.balance = balance;
        this.price = price;
        this.id = id;
        this.description = description;
        this.moveIn = moveIn;
    }

    /**
     * Calculates number of due dates that have passed from the initial move-in date
     * to the current date.
     */
    protected abstract int dueDatesSinceMoveIn();

    /**
     * Returns true if a due date is approaching, false if not.
     */
    protected abstract boolean dueDateApproaching();

    /**
     * Helper method for calculating the duration between the move-in date and the current date
     * (intended to be used by subclasses)
     */
    protected Period rentalPeriod() {
        return Period.between(this.getMoveInDate(), LocalDate.now());
    }

    /**
     * Returns 0 if the rental has been sufficiently payed for, 1 if a due date is approaching
     * (less than 1 week), and 2 if payment is late.
     */
    public int paymentStatus() {
        if (dueDateApproaching())
            return 1;
        if (this.balance < this.price * dueDatesSinceMoveIn())
            return 2;

        return 0;
    }

    public LocalDate getMoveInDate() {
        // LocalDate objects are immutable and so the reference can be returned
        return this.moveIn;
    }

    //getters and setters
    public double getBalance() {
        return balance;
    }

    public double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getMoveIn() {
        return moveIn;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s", getBalance(), getPrice(), getId(), getDescription(),
                getMoveIn());
    }
}
