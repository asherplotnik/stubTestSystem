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
    private String path;
    private String method;
    private HttpHeaders headers;
    private String bodyAsString;
    private long timestamp;


    public InterceptedRequest(String path, String method, HttpHeaders headers, String bodyAsString, long timestamp) {
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.bodyAsString = bodyAsString;
        this.timestamp = timestamp;
    }
}