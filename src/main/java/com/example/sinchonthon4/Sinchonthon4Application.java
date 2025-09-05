package com.example.sinchonthon4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Sinchonthon4Application {

	public static void main(String[] args) {
		SpringApplication.run(Sinchonthon4Application.class, args);
	}

}
