package org.springframework.ai.amazon.bedrock.entity.claude;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmazonBedrockModelResponseClaude {

	String completion;

	@JsonProperty("stop_reason")
	String stopReason;

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	public String getStopReason() {
		return stopReason;
	}

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

}
