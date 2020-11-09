package de.hska.categorycore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CategorycoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategorycoreApplication.class, args);
		while (true) {

		}
	}

}
