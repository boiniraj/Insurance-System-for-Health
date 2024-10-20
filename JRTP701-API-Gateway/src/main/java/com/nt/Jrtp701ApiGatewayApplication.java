package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class Jrtp701ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(Jrtp701ApiGatewayApplication.class, args);
	}

}
