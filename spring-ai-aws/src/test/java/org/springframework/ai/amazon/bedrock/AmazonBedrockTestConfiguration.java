package org.springframework.ai.amazon.bedrock;

import org.springframework.ai.amazon.bedrock.client.AmazonBedrockClient;
import org.springframework.ai.amazon.bedrock.embedding.AmazonBedrockEmbeddingClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AmazonBedrockTestConfiguration {

	@Bean
	public AmazonBedrockClient amazonBedrockClient() {
		return new AmazonBedrockClient();
	}

	@Bean
	public AmazonBedrockEmbeddingClient amazonBedrockEmbeddingClient() {
		return new AmazonBedrockEmbeddingClient();
	}

}
