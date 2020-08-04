package com.rentals.rentalmanager.common;

/**
 * Sent by the client to signal to the server which type of request is going to succeed it
 */
public enum RequestType {
    GET, SEARCH, UPDATE, NEW, DELETE, NEWTENANT, DELETETENANT, MAIL
}
