package com.aqsmallwood.controllers;

import com.aqsmallwood.http.Response;
import com.aqsmallwood.http.exceptions.NotImplementedException;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Controllers are responsible for generating responses. Usually this will mean
// using a Service to interact with/fetch data
public abstract class Controller {
    // Response objects are a String responseBody and int statusCode
    public abstract Response GET(HttpExchange request);
    public abstract Response POST(HttpExchange request);

    // Internally route request to appropriate class method via HTTP method
    public Response handleRequest(HttpExchange request) throws NotImplementedException {
        String requestMethod = request.getRequestMethod();
        if (requestMethod.equals("GET")) {
            return GET(request);
        }
        if (requestMethod.equals("POST")) {
            return POST(request);
        }
        throw new NotImplementedException();
    }

    public static String getRequestBody(HttpExchange request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }
}
