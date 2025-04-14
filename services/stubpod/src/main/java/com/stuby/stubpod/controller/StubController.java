package com.stuby.stubpod.controller;

import com.stuby.stubpod.service.FetchStubResponseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StubController {

    private final FetchStubResponseService service;

    public StubController(FetchStubResponseService service) {
        this.service = service;
    }

    @RequestMapping("/{path:.*stub.*}")
    public String testRequest(HttpServletRequest request) {
        return service.fetchStubResponse(request);
    }

}
