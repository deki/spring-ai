package org.springframework.ai.amazon.bedrock.entity;

import java.util.List;

public record AmazonBedrockModelResponse(Integer inputTextTokenCount, List<AmazonBedrockModelResult> results) {
}
