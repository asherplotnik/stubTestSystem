package com.stuby.stubpod.service;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.api.model.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class SpinUpService {

    private static final String TEST = "-test";
    private static final String CLIENT_URL = "http://java-stub-java-demo-chart:8080/stub";
    private final KubernetesClient kubernetesClient;

    public SpinUpService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public String deployStubPod(String originalServiceName) {
        String namespace = "default";

        Service originalService = getOriginalService(originalServiceName, namespace);

        String image = getORiginalImageString(originalServiceName, originalService, namespace);

        Map<String, String> stubLabels = buildStubLabels(originalServiceName, originalService);
        createService(originalServiceName, originalService, namespace, stubLabels);

        Pod pod = createPod(originalServiceName, image, namespace, stubLabels);

        return pod.getFullResourceName();
    }

    private Pod createPod(String originalServiceName, String image, String namespace, Map<String, String> labels) {
        Pod stubPod = new PodBuilder()
                .withNewMetadata()
                .withName(originalServiceName + TEST)
                .withLabels(labels)
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("stub-container")
                .withImage(image)
                .addNewEnv()
                .withName("client.url")
                .withValue(CLIENT_URL)
                .endEnv()
                .addNewPort()
                .withContainerPort(8080)  // Adjust the port if needed
                .endPort()
                .endContainer()
                .endSpec()
                .build();

        return kubernetesClient.resource(stubPod).inNamespace(namespace).create();
    }

    private void createService(String originalServiceName, Service originalService, String namespace, Map<String, String> stubLabels) {
        Service newService = new ServiceBuilder(originalService)
                .editMetadata()
                .withName(originalServiceName + TEST)
                .withResourceVersion(null)
                .endMetadata()
                .editSpec()
                .withClusterIP(null)
                .withClusterIPs(new ArrayList<>())
                .withType("NodePort")
                .withSelector(stubLabels)
                .editFirstPort()
                .withNodePort(30084)
                .endPort()
                .endSpec()
                .build();
        kubernetesClient.resource(newService).inNamespace(namespace).create();
    }

    private String getORiginalImageString(String originalServiceName, Service originalService, String namespace) {
        Map<String, String> selector = originalService.getSpec().getSelector();
        List<Pod> pods = kubernetesClient.pods().inNamespace(namespace).withLabels(selector).list().getItems();
        if (pods.isEmpty()) {
            throw new IllegalStateException("No pods found matching service " + originalServiceName);
        }

        return pods.get(0).getSpec().getContainers().get(0).getImage();
    }

    private Service getOriginalService(String originalServiceName, String namespace) {
        Service originalService = kubernetesClient.services().inNamespace(namespace).withName(originalServiceName).get();
        if (originalService == null) {
            throw new IllegalArgumentException("Service " + originalServiceName + " not found in namespace default");
        }
        return originalService;
    }

    private Map<String, String> buildStubLabels(String originalServiceName, Service originalService) {
        Map<String, String> stubLabels = new HashMap<>();
        if (originalService.getMetadata().getLabels() != null) {
            stubLabels.putAll(originalService.getMetadata().getLabels());
        }
        stubLabels.put("stub", "true");
        stubLabels.put("app", originalServiceName + TEST);
        return stubLabels;
    }
}
