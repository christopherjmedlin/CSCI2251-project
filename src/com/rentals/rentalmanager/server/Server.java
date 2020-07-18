package com.rentals.rentalmanager.server;

// just to test importing from common
import com.rentals.rentalmanager.common.RentalProperty;

import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        // TODO important! port number should be configurable (by command line args perhaps)
        ServerStart server = new ServerStart(1234);
        try {
            server.run();
        } catch (IOException e) {
            System.err.println("Port is already being used by another application.");
        }
    }
}
