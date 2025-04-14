package com.stuby.stubpod.service;

import com.stuby.stubpod.repository.StubRepository;
import com.stuby.stubpod.repository.entity.StubResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class FetchStubResponseService {

    StubRepository stubRepository;
    public FetchStubResponseService(StubRepository stubRepository) {
        this.stubRepository = stubRepository;
    }

    public String fetchStubResponse(HttpServletRequest request) {
        String testStubId = request.getHeader("testStubId");
        return stubRepository.findById(testStubId)
                .map(StubResponseEntity::getStubResponse)
                .orElse(null);
    }
}
