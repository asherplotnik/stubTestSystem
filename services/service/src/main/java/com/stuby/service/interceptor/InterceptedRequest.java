package com.stuby.service.interceptor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
@Getter
@Setter
@Builder
@ToString
public class InterceptedRequest {
    private String url;
    private String method;
    private HttpHeaders headers;
    private Object body;
    private Object[] uriVariables;
    private long timestamp;


    public InterceptedRequest(String url, String method, HttpHeaders headers, Object body, Object[] uriVariables, long timestamp) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.uriVariables = uriVariables;
        this.timestamp = timestamp;
    }
}