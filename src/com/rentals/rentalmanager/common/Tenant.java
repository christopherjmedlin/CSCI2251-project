package com.rentals.rentalmanager.common;

import java.io.Serializable;

public class Tenant implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Tenant(int id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Tenant(int id, String firstName, String lastName) {
        this(id, firstName, lastName, "", "");
    }

    public Tenant(int id) {
        this(id, "", "");
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getFirstName(), getLastName(), getEmail());
    }

}
