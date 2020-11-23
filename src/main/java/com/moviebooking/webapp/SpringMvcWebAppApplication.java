package com.moviebooking.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class SpringMvcWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcWebAppApplication.class, args);
	}

}
