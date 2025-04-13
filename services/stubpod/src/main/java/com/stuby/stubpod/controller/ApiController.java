package com.stuby.stubpod.controller;

import com.stuby.stubpod.service.GetServicesService;
import com.stuby.stubpod.service.SpinUpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final SpinUpService spinUpService;
    private final GetServicesService getServicesService;
    public ApiController(SpinUpService spinUpService, GetServicesService getServicesService) {
        this.spinUpService = spinUpService;
        this.getServicesService = getServicesService;
    }

    @GetMapping("/get")
    public String getServices() {
        return getServicesService.getServiceNames();
    }

    @GetMapping("/createTestPod")
    public String createTestPod(@RequestParam String name) {
        return spinUpService.deployStubPod(name);
    }
}
