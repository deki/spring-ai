package org.springframework.ai.amazon.bedrock.embedding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.amazon.bedrock.entity.AmazonBedrockEmbedding;
import org.springframework.ai.amazon.bedrock.entity.titan.AmazonBedrockPromptTitanEmbedding;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;

import java.nio.charset.Charset;
import java.util.List;

public class AmazonBedrockEmbeddingClient implements EmbeddingClient {

	private static final Logger logger = LoggerFactory.getLogger(AmazonBedrockEmbeddingClient.class);

	private final BedrockRuntimeClient bedrockRuntimeClient = BedrockRuntimeClient.create();

	private final ObjectMapper objectMapper = new ObjectMapper();

	private String modelId;

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
			AmazonBedrockEmbedding embedding = objectMapper.readValue(bedrockModelResponseString,
					AmazonBedrockEmbedding.class);
			return embedding.embedding();
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

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

}
