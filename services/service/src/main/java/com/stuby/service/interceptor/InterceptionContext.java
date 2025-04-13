package com.stuby.service.interceptor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterceptionContext {
    private final ThreadLocal<List<RequestResponseRecord>> records = ThreadLocal.withInitial(ArrayList::new);

    public List<RequestResponseRecord> getRecords() {
        return records.get();
    }

    public void addRecord(RequestResponseRecord record) {
        records.get().add(record);
    }

    public void clear() {
        records.remove();
    }
}