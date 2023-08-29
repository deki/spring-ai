package org.springframework.ai.amazon.bedrock.embedding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.amazon.bedrock.client.AmazonBedrockClient;
import org.springframework.ai.amazon.bedrock.entity.AmazonBedrockModelResponse;
import org.springframework.ai.amazon.bedrock.entity.claude.AmazonBedrockModelResponseClaude;
import org.springframework.ai.amazon.bedrock.entity.claude.AmazonBedrockPromptClaude;
import org.springframework.ai.amazon.bedrock.entity.titan.AmazonBedrockPromptTitan;
import org.springframework.ai.amazon.bedrock.entity.titan.AmazonBedrockPromptTitanEmbedding;
import org.springframework.ai.amazon.bedrock.entity.titan.AmazonBedrockTitanTextGenerationConfig;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.*;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AmazonBedrockEmbeddingClient implements EmbeddingClient {

	private static final Logger logger = LoggerFactory.getLogger(AmazonBedrockEmbeddingClient.class);

	private final BedrockClient bedrockClient = BedrockClient.create();

	private final BedrockRuntimeClient bedrockRuntimeClient = BedrockRuntimeClient.create();

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final String modelId;

	public AmazonBedrockEmbeddingClient(String modelId) {
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
	public List<Double> embed(String text) {
		return null;
	}

	@Override
	public List<Double> embed(Document document) {
		try {
			String bedrockBody = "";
			if (modelId.startsWith("amazon")) {
				bedrockBody = objectMapper
					.writeValueAsString(new AmazonBedrockPromptTitanEmbedding(document.getContent()));
			}
			var body = InvokeModelRequest.builder()
				.modelId(modelId)
				.body(SdkBytes.fromString(bedrockBody, Charset.defaultCharset()))
				.build();
			InvokeModelResponse invokeModelResponse = bedrockRuntimeClient.invokeModel(body);
			String bedrockModelResponseString = invokeModelResponse.body().asUtf8String();
			return null;
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<List<Double>> embed(List<String> texts) {
		return null;
	}

	@Override
	public EmbeddingResponse embedForResponse(List<String> texts) {
		return null;
	}

}
