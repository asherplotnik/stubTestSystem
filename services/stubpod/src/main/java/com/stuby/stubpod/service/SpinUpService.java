package com.stuby.stubpod.service;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class SpinUpService {

    private static final String TEST = "-test";
    private static final String CLIENT_URL = "http://java-stub-java-stub-chart:8080/stub";
    private final KubernetesClient kubernetesClient;
    private static final String NAMESPACE = "default";

    public SpinUpService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public String deployTestPod(String originalServiceName) {
        deleteIfTestPodExists(originalServiceName);
        io.fabric8.kubernetes.api.model.Service originalService = getOriginalService(originalServiceName, NAMESPACE);
        String image = getORiginalImageString(originalServiceName, originalService, NAMESPACE);
        Map<String, String> stubLabels = buildStubLabels(originalServiceName, originalService);
        Service service = createService(originalServiceName, originalService, NAMESPACE, stubLabels);
        String clientUrl = recreateClientUrl(originalService);
        createPod(originalServiceName, image, NAMESPACE, stubLabels, clientUrl);
        return "http://" + service.getMetadata().getName() + ":"+ service.getSpec().getPorts().get(0).getPort();
    }

    private void deleteIfTestPodExists(String originalServiceName) {
        Pod existing = kubernetesClient.pods().inNamespace(NAMESPACE).withName(originalServiceName + TEST).get();
        if (existing != null) {
            deleteTestResource(originalServiceName + TEST);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createPod(String originalServiceName, String image, String namespace, Map<String, String> labels, String clientUrl) {
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
                .withValue(clientUrl)
                .endEnv()
                .addNewPort()
                .withContainerPort(8080)
                .endPort()
                .endContainer()
                .endSpec()
                .build();

        kubernetesClient.resource(stubPod).inNamespace(namespace).create();
    }

    private Service createService(String originalServiceName, Service originalService, String namespace, Map<String, String> stubLabels) {
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
        return kubernetesClient.resource(newService).inNamespace(namespace).create();
    }

    private String recreateClientUrl(Service originalService) {
        Map<String,String> selector = originalService.getSpec().getSelector();
        List<Pod> pods = kubernetesClient.pods()
                .inNamespace(NAMESPACE)
                .withLabels(selector)
                .list()
                .getItems();
        if (pods.isEmpty()) {
            throw new IllegalStateException("No pods found for service " +
                    originalService.getMetadata().getName());
        }

        Pod pod = pods.get(0);
        List<EnvVar> envs = pod.getSpec()
                .getContainers()
                .get(0)
                .getEnv();
        String fullUrl = envs.stream()
                .filter(e -> "client.url".equals(e.getName()))
                .map(EnvVar::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Env " + "client.url" + " not set on pod " +
                                pod.getMetadata().getName()));

        try {
            URI uri = new URI(fullUrl);
            String path  = uri.getRawPath();
            String query = uri.getRawQuery();
            return CLIENT_URL + path + (query != null ? "?" + query : "");
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URL in env: " + fullUrl, ex);
        }
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

    public String deleteTestResource(String resourceName) {
        if (!resourceName.contains("test")) {
            return "Deletion forbidden. not a test resource.";
        }
        getOriginalService(resourceName,NAMESPACE); //make sure service exists.
        var podDeleted = kubernetesClient
                .pods()
                .inNamespace(NAMESPACE)
                .withName(resourceName)
                .withGracePeriod(0)
                .withPropagationPolicy(DeletionPropagation.FOREGROUND)
                .delete();

        var serviceDeleted = kubernetesClient
                .services()
                .inNamespace(NAMESPACE)
                .withName(resourceName)
                .withGracePeriod(0)
                .withPropagationPolicy(DeletionPropagation.FOREGROUND)
                .delete();
        if (podDeleted != null && serviceDeleted != null) {
            return "Test Pod and Service deleted successfully.";
        } else {
            return "Deletion failed for one or more resources.";
        }
    }
}
