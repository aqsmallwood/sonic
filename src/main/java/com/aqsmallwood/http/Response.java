package com.aqsmallwood.http;

public class Response {
    private int statusCode;
    private String body;

    public Response() {
        this(200, "");
    }

    public Response(int statusCode) {
        this(statusCode, "");
    }

    public Response(String body) {
        this(200, body);
    }

    public Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
