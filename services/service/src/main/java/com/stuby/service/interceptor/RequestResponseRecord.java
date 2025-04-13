package com.stuby.service.interceptor;

import java.util.List;

public class RequestResponseRecord {
    private final List<Object> request;
    private final Object response;

    public RequestResponseRecord(List<Object> request, Object response) {
        this.request = request;
        this.response = response;
    }

    public Object getRequest() {
        return request;
    }

    public Object getResponse() {
        return response;
    }
}




