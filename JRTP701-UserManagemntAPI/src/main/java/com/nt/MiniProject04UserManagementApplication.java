package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class MiniProject04UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniProject04UserManagementApplication.class, args);
	}

}
