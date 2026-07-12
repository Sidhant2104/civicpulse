package com.sidhant.civicpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CivicpulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CivicpulseApplication.class, args);
	}
}
