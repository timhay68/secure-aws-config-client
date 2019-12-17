package com.example.secureconfigclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SecureConfigClientApplication
		//extends SpringBootServletInitializer
{

	public static void main(String[] args) {
		SpringApplication.run(SecureConfigClientApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(SecureConfigClientApplication.class);
//	}
}
