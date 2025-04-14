package com.stuby.stubpod.controller;

import com.stuby.stubpod.model.InterceptedRequest;
import com.stuby.stubpod.service.GetSampleRequestService;
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
    private final GetSampleRequestService getSampleRequestService;
    public ApiController(SpinUpService spinUpService, GetServicesService getServicesService, GetSampleRequestService getSampleRequestService) {
        this.spinUpService = spinUpService;
        this.getServicesService = getServicesService;
        this.getSampleRequestService = getSampleRequestService;
    }

    @GetMapping("/get")
    public String getServices() {
        return getServicesService.getServiceNames();
    }

    @GetMapping("/getSampleRequest")
    public InterceptedRequest getSampleRequest(String serviceName) {
        return getSampleRequestService.getRequest(serviceName);
    }

    @GetMapping("/createTestPod")
    public String createTestPod(@RequestParam String name) {
        return spinUpService.deployTestPod(name);
    }
}
