package com.aqsmallwood.controllers;

import com.aqsmallwood.http.Response;
import com.aqsmallwood.services.TicketService;
import com.sun.net.httpserver.HttpExchange;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import static com.aqsmallwood.http.Contants.HTTP_BAD_REQUEST;
import static com.aqsmallwood.http.Contants.HTTP_NOT_IMPLEMENTED;

public class IndexController extends Controller {

    private TicketService ticketService = new TicketService();

    @Override
    public Response GET(HttpExchange request) {
        String query = request.getRequestURI().getQuery();
        if(query == null) {
            return new Response(HTTP_BAD_REQUEST);
        }
        String command;
        Long ticketId;
        String ticketTitle;
        String responseBody = "";
        try {
            // There should be Util function for creating XML Documents from Strings
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(query)));
            // Check for validity by looking for request parent element
            if(!document.getDocumentElement().getNodeName().equalsIgnoreCase("request")) {
                return new Response(HTTP_BAD_REQUEST);
            }
            // Ideally services would receive command and data and be responsible for determining validity of statement
            command = document.getDocumentElement().getAttribute("command");
            if (command.equalsIgnoreCase("print")) {
                try {
                    ticketId = Long.parseLong(document.getElementsByTagName("ticketid").item(0).getTextContent());
                } catch (Exception e) {
                    ticketId = null;
                }
                responseBody = handlePrint(ticketId);

            }
            if (command.equalsIgnoreCase("add")) {
                ticketTitle = document.getElementsByTagName("tickettitle").item(0).getTextContent();
                responseBody = handleAdd(ticketTitle);
            }
            return new Response(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(HTTP_BAD_REQUEST);

    }

    @Override
    public Response POST(HttpExchange request) {
        return new Response(HTTP_NOT_IMPLEMENTED);
    }

    public String handlePrint(Long ticketId) {
        if (ticketId == null) {
            return ticketService.printTickets();
        }
        return ticketService.printTicket(ticketId);
    }

    public String handleAdd(String ticketTitle) {
        return ticketService.addTicket(ticketTitle);
    }
}
