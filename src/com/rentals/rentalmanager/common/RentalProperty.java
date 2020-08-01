package com.rentals.rentalmanager.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;

public abstract class RentalProperty implements Serializable {
    private double balance;
    private double price;
    private String id;
    private String description;
    private LocalDate moveIn;
    // since tenants are displayed as a list of their names in the client, it seems reasonable to store them in a
    // hashmap with the keys being their full names, appending an id if the name already exists in the hashmap.
    private HashMap<String, Tenant> tenants;

    public RentalProperty(double balance, double price, String id, String description, LocalDate moveIn) {
        this.balance = balance;
        this.price = price;
        this.id = id;
        this.description = description;
        this.moveIn = moveIn;

        this.tenants = new HashMap<>();
    }

    /**
     * Calculates number of due dates that have passed from the initial move-in date
     * to the current date.
     */
    protected abstract int dueDatesSinceMoveIn();

    /**
     * Returns the next due date based on the current date.
     */
    protected abstract LocalDate nextDueDate();

    /**
     * Helper method for calculating the duration between the move-in date and the current date
     * (intended to be used by subclasses)
     */
    protected Period rentalPeriod() {
        return Period.between(this.getMoveInDate(), LocalDate.now());
    }

    public boolean dueDateApproaching() {
        LocalDate dueDate = this.nextDueDate();
        Period per = Period.between(LocalDate.now(), dueDate);
        // TODO make this configurable as something other than a week.
        if (per.getDays() >= 7) {
            return true;
        }
        return false;
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

    /**
     * Associates a new tenant with the property.
     */
    public void addTenant(Tenant tenant) {
        String name = tenant.getFullName();
        if (!this.tenants.containsKey(name))
            this.tenants.put(name, tenant);
            // if the key is already in the map, append the id to ensure uniqueness
            // for example, if "Bob" is already associated with this property, and a tenant with the same name is added to
            // the property, that tenant will appear as "Bob#423", 423 being his id.
        else
            this.tenants.put(name + "#" + tenant.getId(), tenant);
    }

    /**
     * Retrieves the tenant associated with this property with the given id. Since it is a reference being returned,
     * tenants can be updated with this method.
     */
    public Tenant getTenant(String name) {
        return this.tenants.get(name);
    }

    /**
     * Returns true if there are 1 or more tenants, 0 if not.
     */
    public boolean hasTenants() {
        return this.tenants.size() > 0;
    }

    /**
     * Returns the name of each tenant associated with this property.
     */
    public String[] getTenantNames() {
        return this.tenants.keySet().toArray(new String[tenants.size()]);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s", getBalance(), getPrice(), getId(), getDescription(), tenants);

    }
}
