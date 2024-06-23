package com.daeseonbae.DSBBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DsbBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsbBackendApplication.class, args);
	}

}
