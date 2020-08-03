package com.rentals.rentalmanager.common;

public class BillingStatement {
    final String STATEMENT_FORMAT_STRING = "==Billing Statement==\n" +
            "Property ID: %s\n" +
            "Tenants:\n" +
            "%s" +
            "Property Type: %s\n\n" +
            "Next Due Date: %s\n" +
            "Balance: %.00f";
    RentalProperty property;

    public BillingStatement(RentalProperty p) {
        this.property = p;
    }

    public String getStatement() {
        StringBuilder tenants = new StringBuilder();
        for (String s : this.property.getTenantNames()) {
            tenants.append(s).append('\n');
        }
        return String.format(STATEMENT_FORMAT_STRING,
                this.property.getId(),
                tenants.toString(),
                propertyType(),
                this.property.nextDueDate(),
                this.property.calculateBalance()
        );
    }

    private String propertyType() {
        char type = this.property.getId().charAt(0);
        switch (type) {
            case 'A': return "Apartment";
            case 'V': return "Vacation Rental";
            case 'S': return "Home";
        }
        return "";
    }
}
