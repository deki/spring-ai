package org.springframework.ai.amazon.bedrock.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.amazon.bedrock.entity.claude.AmazonBedrockModelResponseClaude;
import org.springframework.ai.amazon.bedrock.entity.claude.AmazonBedrockPromptClaude;
import org.springframework.ai.amazon.bedrock.entity.titan.AmazonBedrockPromptTitan;
import org.springframework.ai.amazon.bedrock.entity.AmazonBedrockModelResponse;
import org.springframework.ai.amazon.bedrock.entity.titan.AmazonBedrockTitanTextGenerationConfig;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.messages.Message;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.*;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AmazonBedrockClient implements AiClient {

	private static final Logger logger = LoggerFactory.getLogger(AmazonBedrockClient.class);

	private final BedrockClient bedrockClient = BedrockClient.create();

	private final BedrockRuntimeClient bedrockRuntimeClient = BedrockRuntimeClient.create();

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final String modelId;

	public AmazonBedrockClient(String modelId, Double temperature) {
		this.modelId = modelId;
		ListFoundationModelsResponse listFoundationModelsResponse = bedrockClient
			.listFoundationModels(ListFoundationModelsRequest.builder().build());
		var modelFound = false;
		for (FoundationModelSummary modelSummary : listFoundationModelsResponse.modelSummaries()) {
			if (modelId.equals(modelSummary.modelId())) {
				modelFound = true;
				break;
			}
		}
		if (!modelFound) {
			throw new IllegalArgumentException("Model ID " + modelId + "not in list of models. Available models: "
					+ listFoundationModelsResponse.modelSummaries()
						.stream()
						.map(FoundationModelSummary::modelId)
						.collect(Collectors.joining(",")));
		}
	}

	@Override
	public AiResponse generate(Prompt prompt) {
		try {
			StringBuilder bedrockPrompt = createBedrockPrompt(prompt);
			String bedrockBody = "";
			if (modelId.startsWith("amazon")) {
				bedrockBody = objectMapper.writeValueAsString(new AmazonBedrockPromptTitan(bedrockPrompt.toString(),
						new AmazonBedrockTitanTextGenerationConfig()));
			}
			else if (modelId.startsWith("anthropic")) {
				bedrockBody = objectMapper.writeValueAsString(new AmazonBedrockPromptClaude(bedrockPrompt.toString()));
			}
			var body = InvokeModelRequest.builder()
				.modelId(modelId)
				.body(SdkBytes.fromString(bedrockBody, Charset.defaultCharset()))
				.build();
			InvokeModelResponse invokeModelResponse = bedrockRuntimeClient.invokeModel(body);
			String bedrockModelResponseString = invokeModelResponse.body().asUtf8String();

			List<Generation> generations = null;
			if (modelId.startsWith("amazon")) {
				generations = objectMapper.readValue(bedrockModelResponseString, AmazonBedrockModelResponse.class)
					.results()
					.stream()
					.map(it -> new Generation(it.outputText(),
							Map.of("tokenCount", it.tokenCount(), "completionReason", it.completionReason())))
					.toList();
			}
			else if (modelId.startsWith("anthropic")) {
				AmazonBedrockModelResponseClaude amazonBedrockModelResponseClaude = objectMapper
					.readValue(bedrockModelResponseString, AmazonBedrockModelResponseClaude.class);
				generations = List.of(new Generation(amazonBedrockModelResponseClaude.getCompletion(),
						Map.of("stopReason", amazonBedrockModelResponseClaude.getStopReason())));
			}
			return new AiResponse(generations);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private static StringBuilder createBedrockPrompt(Prompt prompt) {
		StringBuilder bedrockPrompt = new StringBuilder();
		for (Message message : prompt.getMessages()) {
			switch (message.getMessageType()) {
				case USER -> {
					bedrockPrompt.append(message.getContent().formatted("""
							User: %s
							"""));
				}
				case ASSISTANT -> {
					bedrockPrompt.append(message.getContent().formatted("""
							Assistant: %s
							"""));
				}
				case SYSTEM -> {
					bedrockPrompt.append(message.getContent().formatted("""
							System: %s
							"""));
				}
				case FUNCTION -> {
				}
			}
		}
		return bedrockPrompt;
	}

}
