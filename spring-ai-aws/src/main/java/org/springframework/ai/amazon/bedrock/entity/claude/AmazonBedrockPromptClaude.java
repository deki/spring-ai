package org.springframework.ai.amazon.bedrock.entity.claude;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class AmazonBedrockPromptClaude {

	private String prompt;

	@JsonProperty("max_tokens_to_sample")
	private Integer maxTokensToSample = 300;

	private Double temperature = 0.5;

	@JsonProperty("top_k")
	private Integer topK = 250;

	@JsonProperty("top_p")
	private Double topP = 1d;

	@JsonProperty("stop_sequences")
	private List<String> stopSequences = Collections.singletonList("\n\nHuman:");

	public AmazonBedrockPromptClaude() {
	}

	public AmazonBedrockPromptClaude(String prompt) {
		this.prompt = prompt;
	}

	public AmazonBedrockPromptClaude(String prompt, Integer maxTokensToSample, Double temperature, Integer topK,
			Double topP, List<String> stopSequences) {
		this.prompt = prompt;
		this.maxTokensToSample = maxTokensToSample;
		this.temperature = temperature;
		this.topK = topK;
		this.topP = topP;
		this.stopSequences = stopSequences;
	}

	public String getPrompt() {
		return "\n\nHuman:%s\n\nAnswer:".formatted(prompt);
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public Integer getMaxTokensToSample() {
		return maxTokensToSample;
	}

	public void setMaxTokensToSample(Integer maxTokensToSample) {
		this.maxTokensToSample = maxTokensToSample;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Integer getTopK() {
		return topK;
	}

	public void setTopK(Integer topK) {
		this.topK = topK;
	}

	public Double getTopP() {
		return topP;
	}

	public void setTopP(Double topP) {
		this.topP = topP;
	}

	public List<String> getStopSequences() {
		return stopSequences;
	}

	public void setStopSequences(List<String> stopSequences) {
		this.stopSequences = stopSequences;
	}

}
