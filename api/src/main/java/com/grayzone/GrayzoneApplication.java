package com.grayzone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GrayzoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrayzoneApplication.class, args);
	}

}
