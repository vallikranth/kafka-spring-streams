package com.kafka.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class StreamsApp {

	public static void main(String[] args) {
		SpringApplication.run(StreamsApp.class, args);

	}

}
