package com.aqsmallwood;

public class Server {

    public static void main(String[] args) {
        // Starts a thread which creates an HttpServer
        Thread httpServer = new HttpServerThread();
        httpServer.start();

        // Starts a thread which creates a SocketServer
        Thread telnetServer = new TelnetServerThread();
        telnetServer.start();
    }
}
