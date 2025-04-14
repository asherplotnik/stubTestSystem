package com.stuby.stubpod.model;

public class RequestResponseRecord {
    private final InterceptedRequest request;
    private final String response;

    public RequestResponseRecord(InterceptedRequest request, String response) {
        this.request = request;
        this.response = response;
    }

    public InterceptedRequest getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }
}




