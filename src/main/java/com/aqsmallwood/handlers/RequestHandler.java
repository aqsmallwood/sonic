package com.aqsmallwood.handlers;

import com.aqsmallwood.controllers.Controller;
import com.aqsmallwood.controllers.IndexController;
import com.aqsmallwood.controllers.TicketController;
import com.aqsmallwood.http.Response;
import com.aqsmallwood.http.exceptions.NotImplementedException;
import com.aqsmallwood.http.exceptions.RouteNotFoundException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import static com.aqsmallwood.http.Contants.HTTP_NOT_IMPLEMENTED;
import static com.aqsmallwood.http.Contants.HTTP_NOT_FOUND;

/*
 * Responsible for routing HTTP requests, and writing the response output
 */
public class RequestHandler implements HttpHandler {


    private HashMap<String, Controller> routes = new HashMap<>();

    public RequestHandler() {
        this.routes.put("/", new IndexController());
        this.routes.put("/ticket", new TicketController());
    }

    private Controller getControllerForRoute(String route) throws RouteNotFoundException {
        Controller controller = routes.get(route);
        if(controller != null) {
            return controller;
        }
        // If route is not defined, check for subpaths starting with defined routes
        for(String key : routes.keySet()) {
            if (route.startsWith(key)) {
                return routes.get(key);
            }
        }
        throw new RouteNotFoundException();
    }

    private void setResponse(HttpExchange request, Response response) throws IOException {
        OutputStream os = request.getResponseBody();
        request.getResponseHeaders().add("Date", new Date().toString());
        request.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        request.getResponseHeaders().add("Content-Type", "application/xml");
        if (response.getStatusCode() == HTTP_NOT_IMPLEMENTED || response.getStatusCode() == HTTP_NOT_FOUND) {
            request.sendResponseHeaders(response.getStatusCode(), -1);
            os.close();
        }
        request.sendResponseHeaders(response.getStatusCode(), response.getBody().length());
        os.write(response.getBody().getBytes());
        os.close();
    }

    /* Create
     * Find an appropriate controller for the request, generate the response, and write to the output stream
     */
    @Override
    public void handle(HttpExchange request) throws IOException {
        Response response;
        Controller controller;
        try {
             controller = getControllerForRoute(request.getRequestURI().getPath());
        } catch (RouteNotFoundException e) {
            response = new Response(HTTP_NOT_FOUND);
            this.setResponse(request, response);
            return;
        }
        try {
            response = controller.handleRequest(request);
        } catch (NotImplementedException e) {
            response = new Response(HTTP_NOT_IMPLEMENTED);
        }
        this.setResponse(request, response);
    }
}
