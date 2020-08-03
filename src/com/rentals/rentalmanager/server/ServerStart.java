package com.rentals.rentalmanager.server;

import com.rentals.rentalmanager.common.PropertySearch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ServerStart {
    private static final Logger LOGGER = Logger.getLogger(ServerStart.class.getName());

    private ServerSocket serverSock;
    private ExecutorService exec;
    private Properties config;

    public ServerStart(Properties config) {
        this.exec = Executors.newCachedThreadPool();
        this.config = config;
    }

    public void run(String dbUser, String dbPass) throws IOException {
        this.serverSock = new ServerSocket(
                Integer.parseInt(this.config.getProperty("port", "1234"))
        );
        while (true) {
            Socket sock = serverSock.accept();
            LOGGER.info("Connection accepted from " + sock.getRemoteSocketAddress().toString() + ".");
            exec.execute(new ProcessRequest(
                    sock,
                    this.config
            ));
        }
    }

    // TODO a way to more cleanly stop the server, closing the executor service and server socket
}
