package com.stuby.stubpod.controller;

import com.stuby.stubpod.model.RequestResponseRecord;
import com.stuby.stubpod.repository.entity.StubResponseEntity;
import com.stuby.stubpod.service.GetSampleRequestService;
import com.stuby.stubpod.service.GetServicesService;
import com.stuby.stubpod.service.SetStubResponseService;
import com.stuby.stubpod.service.SpinUpService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final SpinUpService spinUpService;
    private final GetServicesService getServicesService;
    private final SetStubResponseService setStubResponseService;
    public ApiController(SpinUpService spinUpService, GetServicesService getServicesService, SetStubResponseService setStubResponseService) {
        this.spinUpService = spinUpService;
        this.getServicesService = getServicesService;
        this.setStubResponseService = setStubResponseService;
    }

    @GetMapping("/get")
    public Map<String, List<RequestResponseRecord>> getServices() {
        return getServicesService.getServicesWithSampleRequests();
    }

    @PostMapping("/setStubResponse")
    public String setStubResponse(@RequestBody StubResponseEntity stubResponse) {
       return setStubResponseService.setStubResponse(stubResponse);
    }

    @GetMapping("/createTestPod")
    public String createTestPod(@RequestParam String name) {
        return spinUpService.deployTestPod(name);
    }
}
