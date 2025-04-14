package com.stuby.service.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
        Object[] args = joinPoint.getArgs();

        String url = args.length > 0 && args[0] instanceof String urlArgs? urlArgs : null;
        String method = args.length > 1 ? args[1].toString() : null;
        HttpEntity<?> requestEntity = args.length > 2 && args[2] instanceof HttpEntity ? (HttpEntity<?>) args[2] : null;
        Object[] uriVariables = args.length > 4 ? Arrays.copyOfRange(args, 4, args.length) : new Object[0];
        if (!validTestRequest(requestEntity)) {
            return joinPoint.proceed();
        }
        var interceptedRequest = InterceptedRequest.builder()
                .url(url)
                .method(method)
                .headers(requestEntity.getHeaders())
                .body(requestEntity.getBody())
                .uriVariables(uriVariables)
                .timestamp(System.currentTimeMillis())
                .build();
        Object response = null;
        String responseBodyAsString = null;
        try {
            response = joinPoint.proceed();
            return response;
        } finally {
            if(response instanceof ResponseEntity) {
                Object body = ((ResponseEntity<?>) response).getBody();
                responseBodyAsString = (body != null ? body.toString() : "null");
            } else {
                responseBodyAsString = String.valueOf(response);
            }
            interceptionContext.addRecord(new RequestResponseRecord(interceptedRequest, responseBodyAsString));
        }
    }

    private boolean validTestRequest(HttpEntity<?> requestEntity) {
        if (requestEntity == null) {
            return false;
        }
        return requestEntity.getHeaders().getFirst("testStubID") != null;
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