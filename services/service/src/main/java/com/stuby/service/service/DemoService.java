package com.stuby.service.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public String useService(String transID) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("testStubID", transID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return response.getBody() != null ? response.getBody() : "No dog found 😢";
    }

}