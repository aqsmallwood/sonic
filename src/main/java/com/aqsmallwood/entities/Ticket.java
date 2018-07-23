package com.aqsmallwood.entities;

import java.util.Date;

public class Ticket {
    private static long counter = 0;
    private Long id;
    private Date opened;
    private String title;
    private boolean closed;

    public Ticket(String title) {
        this.id = new Long(++counter);
        this.opened = new Date();
        this.title = title;
        this.closed = false;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return id + " - " + this.title;
    }

    public String toXmlString() {
        String xml = String.format("<response status=\"success\">\n" +
                "  <ticketid>%d</ticketid>\n" +
                "  <title>%s</title>\n" +
                "  <datetime>%s</datetime>\n" +
                "</response>", this.id, this.title, this.opened);
        return xml;
    }

    public String asXmlElement() {
        String xml = String.format( "  <ticketid>%d</ticketid>\n" +
                "  <title>%s</title>\n" +
                "  <datetime>%s</datetime>\n", this.id, this.title, this.opened);
        return xml;
    }

    public static String ticketNotFoundXML(Long ticketId) {
        String xml = String.format("<response status=\"failure\">\n" +
                "<error>No ticket found for id: %d</error>\n" +
                "</response>", ticketId);
        return xml;
    }

}



