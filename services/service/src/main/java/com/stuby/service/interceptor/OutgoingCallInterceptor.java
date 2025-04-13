package com.stuby.service.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class OutgoingCallInterceptor {

    private final InterceptionContext interceptionContext;
    private final GlobalInterceptionStore globalInterceptionStore;

    public OutgoingCallInterceptor(InterceptionContext interceptionContext, GlobalInterceptionStore globalInterceptionStore) {
        this.interceptionContext = interceptionContext;
        this.globalInterceptionStore = globalInterceptionStore;
    }

    @Around("execution(* org.springframework.web.client.RestTemplate.exchange(..))")
    public Object interceptCall(ProceedingJoinPoint joinPoint) throws Throwable {
        List<Object> request = joinPoint.getArgs().length > 0 ? Arrays.asList(joinPoint.getArgs()) : null;
        Object response = null;
        try {
            response = joinPoint.proceed();
            return response;
        } finally {
            interceptionContext.addRecord(new RequestResponseRecord(request, response));
        }
    }

    @After("(@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            " @annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            " @annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            " @annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            " @annotation(org.springframework.web.bind.annotation.DeleteMapping)) && " +
            "!execution(* com.stuby.service.interceptor.InterceptionController.*(..))")
    public void afterConsumerRequest() {
        globalInterceptionStore.addRecords(interceptionContext.getRecords());
        interceptionContext.clear();
    }
}