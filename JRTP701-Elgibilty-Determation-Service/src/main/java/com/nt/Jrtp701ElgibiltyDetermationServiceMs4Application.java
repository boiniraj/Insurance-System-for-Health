package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Jrtp701ElgibiltyDetermationServiceMs4Application {

	public static void main(String[] args) {
		SpringApplication.run(Jrtp701ElgibiltyDetermationServiceMs4Application.class, args);
	}

}
