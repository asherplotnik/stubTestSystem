package com.stuby.stubpod.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Stubs")
public class StubResponseEntity {
    @Id
    String testStubId;
    String serviceName;
    String stubResponse;
}
