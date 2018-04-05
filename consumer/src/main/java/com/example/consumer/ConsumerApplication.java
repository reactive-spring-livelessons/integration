package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import reactor.core.publisher.Flux;

@EnableBinding(Sink.class)
@SpringBootApplication
public class ConsumerApplication {

		@StreamListener
		public void process(@Input(Sink.INPUT) Flux<String> incomingStrings) {
				incomingStrings
					.map(String::toUpperCase)
					.subscribe(System.out::println);
		}

		public static void main(String[] args) {
				SpringApplication.run(ConsumerApplication.class, args);
		}
}
