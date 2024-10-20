package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Jrtp701ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Jrtp701ConfigServerApplication.class, args);
	}

}
