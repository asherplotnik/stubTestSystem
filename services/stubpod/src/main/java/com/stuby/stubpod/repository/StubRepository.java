package com.stuby.stubpod.repository;

import com.stuby.stubpod.repository.entity.StubResponseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StubRepository extends MongoRepository<StubResponseEntity, String> {

}
