package com.stuby.stubpod;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class StubpodApplication {
	public static void main(String[] args) {
		log.info("<<<<< TEST SYSTEM INSTANCE >>>>>");
		SpringApplication.run(StubpodApplication.class, args);
	}

}
