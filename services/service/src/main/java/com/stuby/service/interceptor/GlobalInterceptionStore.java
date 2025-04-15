package com.stuby.service.interceptor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class GlobalInterceptionStore {
    private final List<RequestResponseRecord> globalRecords = new CopyOnWriteArrayList<>();

    public void addRecords(List<RequestResponseRecord> records) {
        globalRecords.clear();
        globalRecords.addAll(records);
    }

    public List<RequestResponseRecord> getRecords() {
        return globalRecords;
    }

    public void clear() {
        globalRecords.clear();
    }
}
