package com.stuby.service.interceptor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InterceptionController {

    private final GlobalInterceptionStore globalInterceptionStore;

    public InterceptionController(GlobalInterceptionStore globalInterceptionStore) {
        this.globalInterceptionStore = globalInterceptionStore;
    }

    @GetMapping("/api/interception/records")
    public Map<String, List<RequestResponseRecord>> getInterceptionRecords() {
        Map<String, List<RequestResponseRecord>> map = new HashMap<>();
        List<RequestResponseRecord> list = globalInterceptionStore.getRecords();
        if (list == null) {
            return Collections.emptyMap();
        }
        map.put("data", list);
        return map;
    }

    @GetMapping("/api/interception/clear")
    public String clearInterceptionRecords() {
        globalInterceptionStore.clear();
        return "Context Cleared";
    }
}