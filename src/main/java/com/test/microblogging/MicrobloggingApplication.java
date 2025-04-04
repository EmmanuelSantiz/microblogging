package com.test.microblogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class MicrobloggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrobloggingApplication.class, args);
	}

}
