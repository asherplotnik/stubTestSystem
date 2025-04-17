package com.stuby.stubpod.service;

import com.stuby.stubpod.repository.StubRepository;
import com.stuby.stubpod.repository.entity.StubResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SetStubResponseService {
    StubRepository stubRepository;
    SetStubResponseService(StubRepository stubRepository) {
        this.stubRepository = stubRepository;
    }

    public String setStubResponse(StubResponseEntity stubResponse) {
        try {
            stubRepository.save(createPersistentStubResponse(stubResponse));
            return "Stub Response Saved successfully for service: " + stubResponse.getServiceName();
        } catch (Exception e) {
            return "Stub Response was NOT Saved for service: " + stubResponse.getServiceName();
        }
    }

    private StubResponseEntity createPersistentStubResponse(StubResponseEntity stubResponse) {
        StubResponseEntity stubResponseEntity = new StubResponseEntity();
        stubResponseEntity.setStubResponse(stubResponse.getStubResponse());
        stubResponseEntity.setServiceName(stubResponse.getServiceName());
        stubResponseEntity.setUniqueId(stubResponse.getUniqueId());
        return stubResponseEntity;
    }




}
