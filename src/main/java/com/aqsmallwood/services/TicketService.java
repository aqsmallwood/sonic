package com.aqsmallwood.services;

import com.aqsmallwood.entities.Ticket;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketService {
    private static HashMap<Long, Ticket> tickets = new HashMap<Long, Ticket>();

    private static String responseXml = "<response status=\"%s\">\n" +
            "%s" +
            "</response>";

    static {
        Ticket ticket = new Ticket("Create Java XML Service");
        tickets.put(ticket.getId(), ticket);
    }

    public TicketService() {

    }

    public static Ticket getTicket(Long ticketId) {
        Ticket ticket = tickets.get(ticketId);
        if(ticket != null) {
            return ticket;
        }
        return null;
    }

    public static Ticket putTicket(String title) {
        Ticket ticket = new Ticket(title);
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    public static String printTicket(Long ticketId) {
        Ticket ticket = tickets.get(ticketId);
        if(ticket == null) {
            return String.format(responseXml, "fail", "  <error>No ticket found for ID: " + ticketId + "</error>");
        }
        return String.format(responseXml, "success", ticket.asXmlElement());
    }

    public static String addTicket(String title) {
        Ticket ticket = putTicket(title);
        return String.format(responseXml, "success", ticket.asXmlElement());
    }

    public static String printTickets() {
        StringBuilder sb = new StringBuilder();
        sb.append("  <tickets>\n");

        for(Long key : tickets.keySet()) {
            sb.append("    <ticket>");
            sb.append(tickets.get(key).asXmlElement());
            sb.append("    </ticket>");
        }
        sb.append("  </tickets>\n");
        return sb.toString();
    }

    public String runCommand(String command, Object data) {
        // Ideally this would use reflection to verify valid commands and data
        if (command.equalsIgnoreCase("print")) {
            if (data ==  null) {
                return tickets.toString();
            }
            Ticket ticket = getTicket(Long.parseLong((String)data));
            if(ticket != null) {
                return getTicket(Long.parseLong((String)data)).toString();
            }
            return "Ticket " + data + " does not exist";
        } else if (command.equalsIgnoreCase("add")) {

            addTicket((String)data);
            return "Ticket created";
        }
        return "Command '" + command + "' does not exist";
    }


}
