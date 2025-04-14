package com.stuby.stubpod.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class TestRequestHandler {

    public String handleTestRequest(HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return null;
    }
}
