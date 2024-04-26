package com.soprasteria.flexOfficebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlexOfficeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlexOfficeBackendApplication.class, args);
	}
	

}
