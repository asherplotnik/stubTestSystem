package com.stuby.service.repository;

import lombok.Builder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Setter
@Document(collection = "MyDocument")
public class MyDocument {
    @Id
    private String userId;
    private String str;
}
