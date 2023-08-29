package org.springframework.ai.amazon.bedrock.entity.titan;

public record AmazonBedrockPromptTitan(String inputText, AmazonBedrockTitanTextGenerationConfig textGenerationConfig) {
}
