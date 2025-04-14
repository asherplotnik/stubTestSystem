package com.stuby.stubpod.controller;

import com.stuby.stubpod.service.TestRequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StubController {

    private final TestRequestHandler handler;

    public StubController(TestRequestHandler handler) {
        this.handler = handler;
    }


    @RequestMapping("/**")
    public String catchAll(HttpServletRequest request) {

        return handler.handleTestRequest(request);
    }

    @GetMapping("/stub")
    public String getStub() {
        return "{\"data\": \"this is a stub\"}";
    }
}
