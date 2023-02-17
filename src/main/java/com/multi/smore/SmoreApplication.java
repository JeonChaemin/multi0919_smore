package com.multi.smore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SmoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmoreApplication.class, args);
	}

}
