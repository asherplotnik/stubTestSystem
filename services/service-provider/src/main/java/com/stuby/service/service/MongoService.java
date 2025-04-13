package com.stuby.service.service;

import com.stuby.service.repository.MongoRepo;
import com.stuby.service.repository.MyDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoService {

    private final MongoRepo mongoRepo;

    public MongoService(MongoRepo mongoRepo) {
        this.mongoRepo = mongoRepo;
    }


    public MyDocument get(String id, String transID) {
        var res = mongoRepo.findById(id).orElse(null);
        if (res == null){
            log.info("NO CONTENT FOR ID: {}", id);
        }
        log.info("get result: {}", res);
        return res;
    }

    public MyDocument update(MyDocument obj) {
        var res = mongoRepo.save(obj);
        log.info("save result: {}", res);
        return res;
    }

    public String delete(String id) {
        Long count = mongoRepo.deleteCustomById(id);
        if (count > 0) {
            return "deleted successfully.";
        }
        return "not found.";
    }

}
