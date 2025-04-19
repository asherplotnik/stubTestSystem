package com.stuby.service.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
@Slf4j
@Configuration
public class WebClientFilterConfig {
    private static final String REQUEST_HASH_KEY = "requestHash";

    @Bean
    public ExchangeFilterFunction interceptionFilter(InterceptionContext interceptionContext) {
        return (request, next) -> {
            if (!validRequest(request)) {
                return next.exchange(request);
            }
            String pathAndQuery = getPathAndQuery(request.url().toString());
            String method = request.method().name();
            String bodyAsString = request.attribute("body")
                    .map(Object::toString)
                    .orElse("");

            InterceptedRequest req = InterceptedRequest.builder()
                    .path(pathAndQuery)
                    .method(method)
                    .headers(request.headers())
                    .bodyAsString(bodyAsString)
                    .timestamp(System.currentTimeMillis())
                    .build();

            String requestHash = buildUniqueId(req);
            HttpHeaders newHeaders = new HttpHeaders();
            newHeaders.putAll(request.headers());
            newHeaders.add(REQUEST_HASH_KEY, requestHash);
            req.setHeaders(newHeaders);

            ClientRequest filtered = ClientRequest.from(request)
                    .header(REQUEST_HASH_KEY, requestHash)
                    .build();

            return next.exchange(filtered)
                    .flatMap(response ->
                            response.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .flatMap(bodyStr -> {
                                        log.info(">>>>>>>>>>>> request inserted, {}", req.getHeaders().get(REQUEST_HASH_KEY));
                                        interceptionContext.addRecord(new RequestResponseRecord(req, bodyStr));
                                        ClientResponse wrapped = ClientResponse.from(response)
                                                .body(bodyStr)
                                                .build();
                                        return Mono.just(wrapped);
                                    })
                    );
        };
    }

    private boolean validRequest(ClientRequest request) {
        return request.headers().get("testStubId") != null;
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
}