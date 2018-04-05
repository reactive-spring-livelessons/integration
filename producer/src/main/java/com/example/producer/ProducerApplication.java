package com.example.producer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
@EnableBinding(Source.class)
public class ProducerApplication {

		private final Log log = LogFactory.getLog(getClass());

		@Bean
		ApplicationRunner producer(Source source) {
				return args -> {
						int i = 0;
						while (true) {
								Message<String> message = MessageBuilder.withPayload("Hello, #" + i++).build();
								Thread.sleep(1000);
								this.log.info("sending " + message.getPayload() + ".");
								source.output().send(message);
						}
				};
		}

		public static void main(String[] args) {
				SpringApplication.run(ProducerApplication.class, args);
		}
}
