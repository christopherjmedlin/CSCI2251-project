package com.rentals.rentalmanager.common;

public class Tenant {

    private String tenantFirstName;
    private String tenantLastName;
    private String tenantEmail;


    public Tenant() {

    }

    public String getTenantFirstName() {
        return tenantFirstName;
    }

    public void setTenantFirstName(String tenantFirstName) {
        this.tenantFirstName = tenantFirstName;
    }

    public String getTenantLastName() {
        return tenantLastName;
    }

    public void setTenantLastName(String tenantLastName) {
        this.tenantLastName = tenantLastName;
    }

    public String getTenantEmail() {
        return tenantEmail;
    }

    public void setTenantEmail(String tenantEmail) {
        this.tenantEmail = tenantEmail;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getTenantFirstName(), getTenantLastName(), getTenantEmail());
    }

}
