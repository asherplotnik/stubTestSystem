package com.stuby.service.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@Data
@Service
public class DemoService {
    @Value("${client.url}")
    private String uri;
    private final RestTemplate restTemplate;
    private final WebClient webClientWithFilter;

    public DemoService(RestTemplate restTemplate, @Qualifier("webClientWithFilter")WebClient webClientWithFilter) {
        this.restTemplate = restTemplate;
        this.webClientWithFilter = webClientWithFilter;
    }

    public String useService(String testStubID) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("testStubID", testStubID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response1 = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String response2 = webClientWithFilter.get().uri(uri).headers(h -> h.addAll(headers)).retrieve().bodyToMono(String.class).block();
        List<ResponseEntity<String>> list = new ArrayList<>();
        list.add(response1);
        list.add(new ResponseEntity<>(response2, HttpStatus.OK));
        return list.get(0) == null && list.get(1) == null ? "No dog found 😢" : list.toString();
    }

}