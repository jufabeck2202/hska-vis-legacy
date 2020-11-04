package de.hska.productcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductcoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductcoreApplication.class, args);
		while (true) {
			System.out.println("beju ist besser im kicker als tobi");
		}
	}

}
