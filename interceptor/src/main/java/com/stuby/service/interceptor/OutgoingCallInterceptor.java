package com.stuby.service.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

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
        String pathAndQuery = getPathAndQuery(url);

        String method = args.length > 1 ? args[1].toString() : null;
        HttpEntity<?> requestEntity = args.length > 2 && args[2] instanceof HttpEntity ? (HttpEntity<?>) args[2] : null;
        if (!validTestRequest(requestEntity)) {
            return joinPoint.proceed();
        }
        var interceptedRequest = InterceptedRequest.builder()
                .path(pathAndQuery)
                .method(method)
                .headers(requestEntity.getHeaders())
                .bodyAsString(requestEntity.getBody() != null ? requestEntity.getBody().toString() : null)
                .timestamp(System.currentTimeMillis())
                .build();

        String requestHash = buildUniqueId(interceptedRequest);
        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.putAll(requestEntity.getHeaders());
        newHeaders.add("requestHash", requestHash);
        args[2] = new HttpEntity<>(requestEntity.getBody(), newHeaders);
        interceptedRequest.setHeaders(newHeaders);

        Object response = null;
        String responseBodyAsString = null;
        try {
            response = joinPoint.proceed(args);
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

    private String getPathAndQuery(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getRawPath();
            String query = uri.getRawQuery();
            String result = path + (query != null ? "?" + query : "");
            if (result.startsWith("/stub/")) {
                result = result.substring("/stub".length());
            } else if ("/stub".equals(result)) {
                result = "/";
            }
            return result;
        } catch (URISyntaxException e) {
            return url;
        }
    }

    public static String buildUniqueId(InterceptedRequest request) {
        StringBuilder keyString = new StringBuilder()
                .append(request.getHeaders().get("testStubID")).append("\n")
                .append(request.getPath()).append("\n")
                .append(request.getMethod()).append("\n")
                .append(request.getBodyAsString()).append("\n");
        int hash = keyString.toString().hashCode();
        return Integer.toHexString(hash);
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