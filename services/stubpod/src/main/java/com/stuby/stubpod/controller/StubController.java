package com.stuby.stubpod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StubController {

    @GetMapping("/stub")
    public String getStub() {
        return "{\"data\": \"this is a stub\"}";
    }
}
