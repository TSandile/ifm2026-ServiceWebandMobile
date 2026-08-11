package com.mshenguDev.hfservice;

import org.springframework.boot.SpringApplication;

public class TestHfserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(HfserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
