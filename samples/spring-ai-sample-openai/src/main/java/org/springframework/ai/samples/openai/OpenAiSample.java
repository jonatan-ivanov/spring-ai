package org.springframework.ai.samples.openai;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpenAiSample {

	private static final Logger logger = LoggerFactory.getLogger(OpenAiSample.class);

	public static void main(String[] args) {
		SpringApplication.run(OpenAiSample.class, args);
	}

	@Bean
	CommandLineRunner example(ChatClient.Builder builder, PrometheusMeterRegistry meterRegistry) {
		return args -> {
			runChatClientExample(builder.build(), new InMemoryChatMemory());
			System.out.println(meterRegistry.scrape());
		};
	}

	private void runChatClientExample(ChatClient chatClient, ChatMemory chatMemory) {
		ChatResponse response = chatClient.prompt()
				.options(OpenAiChatOptions.builder().withMaxTokens(500).build())
				.user("Tell me a joke")
				.advisors(new PromptChatMemoryAdvisor(chatMemory))
				.call()
				.chatResponse();

		logger.info("Response: {}", response.getResult().getOutput().getContent());
	}
}
