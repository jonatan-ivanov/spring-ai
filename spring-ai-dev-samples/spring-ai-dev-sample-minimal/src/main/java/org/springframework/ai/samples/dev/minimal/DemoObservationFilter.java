package org.springframework.ai.samples.dev.minimal;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import org.springframework.ai.chat.client.observation.ChatClientObservationContext;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DemoObservationFilter implements ObservationFilter {

	@NonNull
	@Override
	public Observation.Context map(@NonNull Observation.Context context) {
		if (context instanceof ChatClientObservationContext chatClientObservationContext) {
			String prompt = prompt(chatClientObservationContext);
			String completion = completion(chatClientObservationContext);
			// "gen_ai.prompt" and "gen_ai.completion" (OTel) also work
			chatClientObservationContext.addHighCardinalityKeyValue(KeyValue.of("input", prompt));
			chatClientObservationContext.addHighCardinalityKeyValue(KeyValue.of("output", completion));
		}
		else if (context instanceof ChatModelObservationContext chatModelObservationContext) {
			String prompt = prompt(chatModelObservationContext);
			String completion = completion(chatModelObservationContext);
			// "gen_ai.prompt" and "gen_ai.completion" (OTel) also work
			chatModelObservationContext.addHighCardinalityKeyValue(KeyValue.of("input", prompt));
			chatModelObservationContext.addHighCardinalityKeyValue(KeyValue.of("output", completion));
		}

		return context;
	}

	private String prompt(ChatClientObservationContext context) {
		return messagesToString(context.getRequest().prompt().getInstructions());
	}

	private String prompt(ChatModelObservationContext context) {
		return messagesToString(context.getRequest().getInstructions());
	}

	private String messagesToString(List<Message> messages) {
		return messages.stream().map(Message::getText).collect(Collectors.joining("\n"));
	}

	private String completion(ChatClientObservationContext context) {
		if (context.getResponse() == null || context.getResponse().chatResponse() == null) {
			return "";
		}
		return generationsToString(context.getResponse().chatResponse().getResults());
	}

	private String completion(ChatModelObservationContext context) {
		if (context.getResponse() == null) {
			return "";
		}
		return generationsToString(context.getResponse().getResults());
	}

	private String generationsToString(List<Generation> generations) {
		return generations.stream().map(Generation::getOutput).map(Message::getText).collect(Collectors.joining("\n"));
	}

}
