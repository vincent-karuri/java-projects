package com.example.servingwebcontent.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.servingwebcontent")
public class SampleWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleWebAppApplication.class, args);
	}
}
