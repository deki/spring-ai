package org.springframework.ai.amazon.bedrock.entity;

import java.util.List;

public record AmazonBedrockEmbedding(Integer inputTextTokenCount, List<Double> embedding) {
}
