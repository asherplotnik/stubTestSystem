package com.stuby.stubpod.service;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetServicesService {

    private final KubernetesClient kubernetesClient;
    public GetServicesService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public String getServiceNames() {
        List<io.fabric8.kubernetes.api.model.Service> services = kubernetesClient.services().inNamespace("default").list().getItems();
        return services.stream()
                .map(service -> service.getMetadata().getName())
                .collect(Collectors.joining(", "));
    }

}
