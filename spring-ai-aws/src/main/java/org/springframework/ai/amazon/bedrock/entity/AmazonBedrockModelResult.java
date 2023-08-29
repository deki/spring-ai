package org.springframework.ai.amazon.bedrock.entity;

public record AmazonBedrockModelResult(Integer tokenCount, String outputText, String completionReason) {
}
