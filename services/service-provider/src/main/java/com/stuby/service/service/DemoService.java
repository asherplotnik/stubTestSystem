package com.stuby.service.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
public class DemoService {
    @Value("${client.url}")
    private String uri;
    private RestTemplate restTemplate;

    public DemoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String useService() {
        String response = restTemplate.getForObject(uri, String.class);
        return response != null ? response : "No dog found 😢";
    }

}