package com.rentals.rentalmanager.server;

import com.rentals.rentalmanager.common.RequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * The ProcessRequest task reads a request from a socket, processes it, and returns an appropriate response
 */
public class ProcessRequest implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ProcessRequest.class.getName());

    Socket sock;
    ObjectInputStream in;
    ObjectOutputStream out;

    /**
     * @param sock reference to the socket which this task will read a request from
     */
    public ProcessRequest(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        try {
            getInputAndOutputStreams();
            RequestType type = (RequestType) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe(e.toString());
        }
    }

    // sets in and out to the output stream and input stream from the socket
    private void getInputAndOutputStreams() throws IOException {
        this.out = new ObjectOutputStream(this.sock.getOutputStream());
        this.in = new ObjectInputStream(this.sock.getInputStream());
    }

    // based on the request type, passes control to another appropriate method
    private void handle(RequestType type) throws IOException {
        out.writeBoolean(true);
    }
}
