package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
public class ClinicCenterApplication {

	public static void main(String[] args) { SpringApplication.run(ClinicCenterApplication.class, args); }

}
