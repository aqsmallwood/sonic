package com.aqsmallwood.controllers;

import com.aqsmallwood.entities.Ticket;
import com.aqsmallwood.http.Response;
import com.aqsmallwood.services.TicketService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.aqsmallwood.http.Contants.HTTP_BAD_REQUEST;
import static com.aqsmallwood.http.Contants.HTTP_CREATED;

public class TicketController extends Controller {

    private static String route = "/ticket";

    private TicketService ticketService = new TicketService();

    @Override
    public Response GET(HttpExchange request) {
        Long ticketId = getParamsForRequest(request);
        return new Response(ticketService.printTicket(ticketId));
    }

    @Override
    public Response POST(HttpExchange request) {
        try {
            String requestBody = getRequestBody(request);
            // The requestBody should be transformed/actually read
            // This is a very simple POST implementation
            return new Response(HTTP_CREATED, new Ticket(requestBody).toString());
        } catch (IOException e) {
            return new Response(HTTP_BAD_REQUEST);
        }
    }

    private static Long getParamsForRequest(HttpExchange request) {
        // This is URL based primary key specification
        // i.e. /tickets/12345/ -> 12345
        return Long.parseLong(request.getRequestURI().toString().split(route)[1].split("/")[1]);
    }
}
