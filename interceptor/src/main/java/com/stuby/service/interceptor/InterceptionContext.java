package com.stuby.service.interceptor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InterceptionContext {
    private final List<RequestResponseRecord> records = Collections.synchronizedList(new ArrayList<>());

    public List<RequestResponseRecord> getRecords() {
        synchronized (records) {
            return new ArrayList<>(records);
        }
    }

    public void addRecord(RequestResponseRecord record) {
        records.add(record);
    }

    public void clear() {
        records.clear();
    }
}