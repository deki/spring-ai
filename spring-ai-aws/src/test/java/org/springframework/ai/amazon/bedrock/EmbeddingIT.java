package org.springframework.ai.amazon.bedrock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.ai.amazon.bedrock.embedding.AmazonBedrockEmbeddingClient;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EmbeddingIT {

	private static final String MODEL_ID = "amazon.titan-embed-text-v1";

	@Autowired
	private AmazonBedrockEmbeddingClient bedrockClient;

	@Test
	void clientTest() {
		bedrockClient.setModelId(MODEL_ID);
		Document doc = new Document("Hello from Spring AI!");
		List<Double> doubles = bedrockClient.embed(doc);
		Assertions.assertNotNull(doubles);
		System.out.println(doubles);
	}

}
