package com.rentals.rentalmanager.tests;

import com.rentals.rentalmanager.common.RequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Integration test for the server (assumes it is currently running on the same machine)
 */
public class ServerTest {
    static Socket s;

    public static void main(String[] args) throws IOException {
        try {
            s = new Socket(InetAddress.getByName(null), 1234);
        } catch (IOException e) {
            System.err.println("Server is not running.");
            System.exit(1);
        }

        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());

        // TODO obviously this test will have to be significantly altered once the server does something more
        out.writeObject(RequestType.GET);

        assert in.readBoolean();
        System.out.println("Server test passed.");
    }
}