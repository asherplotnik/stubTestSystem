package com.stuby.service.controllers;

import com.stuby.service.repository.MyDocument;
import com.stuby.service.service.DemoService;
import com.stuby.service.service.MongoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final DemoService demoService;

    private final MongoService mongoService;

    public DemoController(DemoService demoService, MongoService mongoService) {
        this.demoService = demoService;
        this.mongoService = mongoService;
    }

    @GetMapping("/call")
    public String getMessage(@RequestHeader("X-IDB-TransactionID") String transID) {
        return demoService.useService(transID);
    }

    @GetMapping("/get")
    public MyDocument get(@RequestParam String id) {
        return mongoService.get(id);
    }

    @PostMapping("/update")
    public MyDocument update(@RequestBody MyDocument obj) {
        return mongoService.update(obj);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String id) {
        return mongoService.delete(id);
    }

}
