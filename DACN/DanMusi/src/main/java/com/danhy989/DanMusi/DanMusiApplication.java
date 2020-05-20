package com.danhy989.DanMusi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DanMusiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanMusiApplication.class, args);
	}

}
