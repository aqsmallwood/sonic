package com.aqsmallwood;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.aqsmallwood.handlers.RequestHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Creates a ServerSocket listening on SERVER_PORT
 */
public class TelnetServerThread extends Thread {
    private static final String SERVER_PORT = System.getProperty("telnetPort", "7000");
    // The port listening for connections
    private static ServerSocket serverSocket;

    public TelnetServerThread() {
        int port;
        try {
            port = Integer.parseInt(SERVER_PORT);
        } catch (NumberFormatException e) {
            port = 7000;
        }
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            serverSocket = null;
        }
    }

    public void run() {
        System.out.println("Server listening for telnet connections on " + SERVER_PORT);
        // Continously listen for connections to the server, and create Threads for the socket connections
        if(serverSocket != null) {
            boolean serverRunning = true;
            Socket socket = null;
            while (serverRunning) {
                try {
                    socket = serverSocket.accept();
                    System.out.println("Connected to new client");
                    System.out.println("Creating new thread for connection");
                    // Should be keeping track of a total number of threads
                    Thread t = new TelnetConnection(socket);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void finalize() throws Throwable {
        try{
            serverSocket.close();
        } finally {
            super.finalize();
        }
    }


}