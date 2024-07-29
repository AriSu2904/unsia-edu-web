package com.unsia.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UnsiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnsiaApplication.class, args);
	}

}
