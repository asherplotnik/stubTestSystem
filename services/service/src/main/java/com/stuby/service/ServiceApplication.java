package com.stuby.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class ServiceApplication {
	public static void main(String[] args) {
		log.info("<<<<< CONSUMER INSTANCE >>>>>");
		SpringApplication.run(ServiceApplication.class, args);
	}
}
