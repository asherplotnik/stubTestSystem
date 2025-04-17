package com.stuby.service.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


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
        ResponseEntity<String> response1 = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        List<ResponseEntity<String>> list = new ArrayList<>();
        list.add(response1);
        list.add(response2);
        return list.get(0) == null && list.get(1) == null ? "No dog found 😢" : list.toString();
    }

}