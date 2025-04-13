package com.stuby.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoRepo extends MongoRepository<MyDocument, String> {

    @Query(value = "{ '_id': ?0 }", delete = true)
    Long deleteCustomById(String id);


}
