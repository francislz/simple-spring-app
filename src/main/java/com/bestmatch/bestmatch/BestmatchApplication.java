package com.bestmatch.bestmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BestmatchApplication {

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(BestmatchApplication.class, args);
	}

}
