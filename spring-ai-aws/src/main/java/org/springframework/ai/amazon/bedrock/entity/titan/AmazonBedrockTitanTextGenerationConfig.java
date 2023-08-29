package org.springframework.ai.amazon.bedrock.entity.titan;

import java.util.Collections;
import java.util.List;

public class AmazonBedrockTitanTextGenerationConfig {

	private Integer maxTokenCount = 512;

	private List<String> stopSequences = Collections.emptyList();

	private Double temperature = 0.1;

	private Double topP = 0.9;

	public AmazonBedrockTitanTextGenerationConfig() {
	}

	public Integer getMaxTokenCount() {
		return maxTokenCount;
	}

	public void setMaxTokenCount(Integer maxTokenCount) {
		this.maxTokenCount = maxTokenCount;
	}

	public List<String> getStopSequences() {
		return stopSequences;
	}

	public void setStopSequences(List<String> stopSequences) {
		this.stopSequences = stopSequences;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getTopP() {
		return topP;
	}

	public void setTopP(Double topP) {
		this.topP = topP;
	}

}
