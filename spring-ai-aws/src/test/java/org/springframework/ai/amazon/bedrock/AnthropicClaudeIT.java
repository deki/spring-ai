package org.springframework.ai.amazon.bedrock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.amazon.bedrock.client.AmazonBedrockClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnthropicClaudeIT {

	private static final String MODEL_ID = "anthropic.claude-v2";

	private static final String PROMPT = """
			    Generative AI refers to artificial intelligence systems that are capable of generating
			    novel content such as text, images, audio, video, and more, as opposed to just analyzing
			    existing data. The key aspect of generative AI is that it creates brand new outputs that
			    are unique and original, not just reproductions or remixes of existing content. Generative
			    AI leverages machine learning techniques like neural networks that are trained on large
			    datasets to build an understanding of patterns and structures within the data. It then
			    uses that knowledge to generate new artifacts that conform to those patterns but are not
			    identical copies, allowing for creativity and imagination. Prominent examples of generative
			    AI include systems like DALL-E that creates images from text descriptions, GPT-3 that
			    generates human-like text, and WaveNet that produces realistic synthetic voices. Generative
			    models hold great promise for assisting and augmenting human creativity across many domains
			    but also raise concerns about potential misuse if not thoughtfully implemented. Overall,
			    generative AI aims to mimic human creative abilities at scale to autonomously produce
			    high-quality, diverse, and novel content.

			    Please create a single short paragraph so that a fiver years old child can understand.
			""";

	@Autowired
	private AmazonBedrockClient bedrockClient;

	@Test
	void clientTest() {
		bedrockClient.setModelId(MODEL_ID);
		UserMessage userMessage = new UserMessage(PROMPT);
		AiResponse response = bedrockClient.generate(new Prompt(userMessage));
		String generatedText = response.getGeneration().getText();
		Assertions.assertNotNull(generatedText);
		System.out.println(generatedText);
	}

}
