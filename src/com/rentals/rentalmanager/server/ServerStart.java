package com.rentals.rentalmanager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStart {
    int port;
    ServerSocket serverSock;
    ExecutorService exec;

    public ServerStart(int port) {
        this.port = port;
        this.exec = Executors.newCachedThreadPool();
    }

    public void run() throws IOException {
        this.serverSock = new ServerSocket(this.port);
        while (true) {
            Socket sock = serverSock.accept();
            exec.execute(new ProcessRequest(sock));
        }
    }

    // TODO a way to more cleanly stop the server, closing the executor service and server socket
}
