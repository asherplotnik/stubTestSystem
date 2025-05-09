package com.stuby.stubpod.service;

import com.stuby.stubpod.model.RequestResponseRecord;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ServiceResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GetSampleRequestService {

    private final KubernetesClient kubernetesClient;
    private final RestTemplate restTemplate;

    public GetSampleRequestService(KubernetesClient kubernetesClient, RestTemplate restTemplate) {
        this.kubernetesClient = kubernetesClient;
        this.restTemplate = restTemplate;
    }

    public Map<String, List<RequestResponseRecord>> getRequest(String serviceName) {
        if (serviceName.contains("mongo") || serviceName.contains("stub") ||serviceName.contains("kubernetes")) {
            return Map.of(serviceName, Collections.emptyList());
        }
        String url = buildRequestUrl(serviceName);
        return restTemplate.getForObject(url, Map.class);
    }


    private String buildRequestUrl(String originalServiceName) {
        String port = getPort(originalServiceName);
        return "http://" + originalServiceName + ":" + port + "/api/interception/records";
    }

    private String getPort(String originalServiceName) {
        ServiceResource<io.fabric8.kubernetes.api.model.Service> originalServiceResource =
                kubernetesClient.services().inNamespace("default").withName(originalServiceName);
        if (originalServiceResource == null) {
            return "8080";
        }
        return originalServiceResource.get().getSpec().getPorts().stream()
                .map(servicePort -> servicePort.getPort().toString())
                .filter(portString -> portString.startsWith("80"))
                .findFirst()
                .orElse("8080");
    }

}
