package com.rentals.rentalmanager.common;

public class BillingStatement {
    final String STATEMENT_FORMAT_STRING = "<h1>Billing Statement</h1>\n" +
            "<b>Property ID:</b> %s<br />" +
            "<b>Tenants:</b><br />" +
            "%s" +
            "<b>Property Type:</b> %s<br /><br />" +
            "<b>Next Due Date:</b> %s<br />" +
            "<b>Balance:</b> $%.00f";
    RentalProperty property;

    public BillingStatement(RentalProperty p) {
        this.property = p;
    }

    public String getStatement() {
        StringBuilder tenants = new StringBuilder();
        for (String s : this.property.getTenantNames()) {
            tenants.append(s).append(", ");
        }
        tenants.append("<br />");
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
