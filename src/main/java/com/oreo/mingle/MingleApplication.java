package com.oreo.mingle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MingleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MingleApplication.class, args);
	}

}
