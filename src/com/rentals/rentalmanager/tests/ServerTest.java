package com.rentals.rentalmanager.tests;

import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.RequestType;
import com.rentals.rentalmanager.common.VacationRental;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Integration test for the server (assumes it is currently running on the same machine)
 */
public class ServerTest {
    private static Logger LOGGER = Logger.getLogger("testing");
    private static Socket s;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        testNew();
        //testGet();
    }

    private static void connect() throws IOException {
        try {
            s = new Socket(InetAddress.getByName(null), 1234);
        } catch (IOException e) {
            System.err.println("Server is not running.");
            System.exit(1);
        }

        out = new ObjectOutputStream(s.getOutputStream());
        in = new ObjectInputStream(s.getInputStream());
    }

    private static void close() throws IOException {
        in.close();
        out.close();
        s.close();
    }

    /*
    i do not want to make any assertions here as I don't want to have to recreate the DB everytime I run this test
     */
    private static void testNew() throws IOException {
        connect();
        out.writeObject(RequestType.NEW);
        out.writeObject("VABQ555");
        // just to wait for completion
        in.readBoolean();

        close();
    }

    private static void testGet() throws IOException, ClassNotFoundException {
        connect();
        out.writeObject(RequestType.GET);
        out.writeObject("VABQ555");
        assert in.readBoolean();
        RentalProperty property = (RentalProperty) in.readObject();
        assert property.getId() == "VABQ555";
        assert property instanceof VacationRental;
        close();
    }
}