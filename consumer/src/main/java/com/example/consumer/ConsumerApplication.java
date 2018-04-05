package com.example.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;

@Log
@SpringBootApplication
public class ConsumerApplication {

		@EnableBinding(Sink.class)
		public static class StreamListenerSink {
				
				@StreamListener
				public void process(@Input(Sink.INPUT) Flux<String> messages) {
						messages.subscribe(g -> log.info("new greeting: " + g));
				}
		}

		@Bean
		IntegrationFlow flow() {

				Flux<Greeting> delayElements = Flux
					.<Greeting>generate(sink -> sink.next(new Greeting("Hello, world @" + Instant.now().toString() + "!")))
					.delayElements(Duration.ofSeconds(1));

				return IntegrationFlows.from(delayElements
					.map(g -> MessageBuilder.withPayload(g).build()))
					.handle((GenericHandler<Greeting>) (payload, headers) -> {
							log.info("new Publisher<T>-powered message: " + payload.getMessage());
							return null;
					})
					.get();
		}

		public static void main(String[] args) {
				SpringApplication.run(ConsumerApplication.class, args);
		}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Greeting {
		private String message;
}