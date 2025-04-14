package com.stuby.stubpod.service;

import com.stuby.stubpod.model.RequestResponseRecord;
import io.fabric8.kubernetes.client.KubernetesClient;
import java.util.Collections;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetServicesService {

    private final GetSampleRequestService getSampleRequestService;
    private final KubernetesClient kubernetesClient;
    public GetServicesService(KubernetesClient kubernetesClient, GetSampleRequestService getSampleRequestService) {
        this.getSampleRequestService = getSampleRequestService;
        this.kubernetesClient = kubernetesClient;
    }

    public Map<String, List<RequestResponseRecord>> getServicesWithSampleRequests() {
        Map<String, List<RequestResponseRecord>> mapByServiceName = new HashMap<>();
        List<io.fabric8.kubernetes.api.model.Service> services = kubernetesClient.services().inNamespace("default").list().getItems();

        services.stream()
                .map(service -> service.getMetadata().getName())
                .forEach(service -> mapByServiceName.put(service, getRequests(service)));
        return mapByServiceName;
    }

    private List<RequestResponseRecord> getRequests(String service) {
        try {
            var res =  getSampleRequestService.getRequest(service);
            if (res != null && res.get("data") != null) {
                return res.get("data");
            }
            return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}
