/*
 * Copyright 2023-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ai.samples.dev.minimal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.observation.DemoObservationFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MinimalDevSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinimalDevSampleApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ChatClient.Builder builder) {
		return args -> builder.build().prompt().user("Tell me a joke").call().chatResponse();
//		 return args -> builder.build().prompt().user("Tell me a joke").stream().chatResponse().blockLast();
	}

	@Bean
	DemoObservationFilter demoObservationFilter() {
		return new DemoObservationFilter();
	}

	// in case you want to track things in the logs
	// @Bean
	// ObservationTextPublisher observationTextPublisher() {
	// return new ObservationTextPublisher();
	// }

}
