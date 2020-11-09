package de.hska.usercore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsercoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsercoreApplication.class, args);
		while (true) {
			
		}
	}

}
