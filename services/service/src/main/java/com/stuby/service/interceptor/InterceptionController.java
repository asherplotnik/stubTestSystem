package com.stuby.service.interceptor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class InterceptionController {

    private final GlobalInterceptionStore globalInterceptionStore;

    public InterceptionController(GlobalInterceptionStore globalInterceptionStore) {
        this.globalInterceptionStore = globalInterceptionStore;
    }

    @GetMapping("/api/interception/records")
    public List<RequestResponseRecord> getInterceptionRecords() {
        return globalInterceptionStore.getRecords();
    }

    @GetMapping("/api/interception/clear")
    public String clearInterceptionRecords() {
        globalInterceptionStore.clear();
        return "Context Cleared";
    }
}