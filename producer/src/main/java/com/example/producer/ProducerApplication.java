package com.example.producer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EnableBinding(ProducerChannels.class)
@SpringBootApplication
public class ProducerApplication {

		@Bean
		ApplicationRunner producer(ProducerChannels channels) {
				return args -> {
						MessageChannel output = channels.greetings();
						ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
						service
							.scheduleWithFixedDelay(() -> {
									Message<String> message = MessageBuilder.withPayload("hello, @ " + Instant.now().toString()).build();
									output.send(message);
							}, 1, 1, TimeUnit.SECONDS);
				};
		}

		public static void main(String[] args) {
				SpringApplication.run(ProducerApplication.class, args);
		}
}


interface ProducerChannels {

		@Output
		MessageChannel greetings();
}