package com.aqsmallwood;

import com.aqsmallwood.services.TicketService;
import com.sun.org.apache.xalan.internal.xsltc.runtime.ErrorMessages_zh_CN;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Handles a single socket connection
 *
 */
public class TelnetConnection extends Thread
{
    private static TicketService ticketService = new TicketService();
    private static HashMap<String, Object> services = new HashMap<>();
    private final Socket socket;

    static {
        services.put("tickets", ticketService);
    }

    public void run() {
        boolean connected = true;
        String response;
        while (connected) {
            try {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine().toLowerCase();
                System.out.println("Got input " + line);

                OutputStream output = socket.getOutputStream();
                String service, command;
                Object data = null;

                // Try to parse out space-delimited command
                // ex syntax `tickets print 1`
                String[] statement = line.split(" ", 3);
                service = statement[0];
                if (service.equals("quit") || service.equals("close")) {
                    socket.close();
                    connected = false;
                    continue;
                }

                if (!services.containsKey(service)) {
                    response = "Service " + service + " doesn't exist";
                } else {
                    try {
                        command = statement[1];
                        // If there is a second space, the rest of the line is the final argument
                        if (statement.length > 2) {
                            data = statement[2].split("\n")[0];
                        }
                        // There should be a parent Service class so this isn't so hard-coded
                        response = ((TicketService) services.get(service)).runCommand(command, data);
                    } catch (Exception e) {
                        response = "Invalid Command";
                    }
                }

                PrintWriter writer = new PrintWriter(output, true);
                writer.println(response);
                System.out.println("Wrote output");
                System.out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
                connected = false;
            }
        }
        System.out.println("Closing connection");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TelnetConnection(Socket socket) {
        this.socket = socket;
    }

}

