package com.stuby.stubpod.service;

import com.stuby.stubpod.model.InterceptedRequest;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

@Service
public class GetSampleRequestService {

    private final KubernetesClient kubernetesClient;

    public GetSampleRequestService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public InterceptedRequest getRequest(String serviceName) {
        String url = buildRequestUrl(serviceName, "default");
        return getRecordedRequest(url);
    }

    private InterceptedRequest getRecordedRequest(String url) {
        return null;
    }

    private String buildRequestUrl(String originalServiceName, String namespace) {
        io.fabric8.kubernetes.api.model.Service originalService = kubernetesClient.services().inNamespace(namespace).withName(originalServiceName).get();
        String port = getPort(originalService);
        return "http://" + originalServiceName + ":" + port + "/api/interception/records";
    }

    private String getPort(io.fabric8.kubernetes.api.model.Service originalService) {
        return originalService.getSpec().getPorts().stream()
                .map(servicePort -> servicePort.getPort().toString())
                .filter(portString -> portString.startsWith("80"))
                .findFirst()
                .orElse("8080");
    }

}
