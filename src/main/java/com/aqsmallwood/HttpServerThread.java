package com.aqsmallwood;

import com.aqsmallwood.handlers.RequestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


public class HttpServerThread extends Thread {
    private static int port, threads;
    private static final String SERVER_PORT = System.getProperty("httpPort", "5000");
    private static final String SERVER_THREADS = System.getProperty("httpThreads", "5");
    private static HttpServer httpServer;

    static {
        try {
            port = Integer.parseInt(SERVER_PORT);
            threads = Integer.parseInt(SERVER_THREADS);
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (NumberFormatException e) {
            port = 7000;
            threads = 5;
        } catch (IOException e) {
            httpServer = null;
        }
    }

    public void run() {
        // Create a global context, let the RequestHandler figure out routing
        httpServer.createContext("/", new RequestHandler());
        // Enables multithreaded request handling
        httpServer.setExecutor(Executors.newFixedThreadPool(threads));
        System.out.println("Server listening for HTTP connections on " + SERVER_PORT);
        System.out.println("\nQuick Links\n"
                + "http://localhost:" + SERVER_PORT + "/?<request command=\"print\"></request>\n"
                + "http://localhost:" + SERVER_PORT + "/?<request command=\"print\"><ticketid>1</ticketid></request>\n"
                + "http://localhost:" + SERVER_PORT + "/?<request command=\"add\"><tickettitle>New Ticket</tickettitle></request>\n"
                + "http://localhost:" + SERVER_PORT + "/ticket/1");
        httpServer.start();
    }


    public void finalize() throws Throwable {
        try{
            httpServer.stop(0);
        } finally {
            super.finalize();
        }
    }
}
