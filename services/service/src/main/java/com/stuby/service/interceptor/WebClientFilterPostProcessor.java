package com.stuby.service.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFilterPostProcessor implements BeanPostProcessor {

    private final ExchangeFilterFunction interceptionFilter;

    public WebClientFilterPostProcessor(ExchangeFilterFunction interceptionFilter) {
        this.interceptionFilter = interceptionFilter;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WebClient webClientBean) {
            return (webClientBean)
                    .mutate()
                    .filter(interceptionFilter)
                    .build();
        }
        return bean;
    }
}
