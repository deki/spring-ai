package org.springframework.ai.amazon.bedrock.entity.ai21labs;

import java.util.Collections;
import java.util.List;

public record AmazonBedrockJurassicRequestBody(String prompt, Integer maxTokens, Double temperature, Double topP, List<String> stopSequences,
                                               Penalty countPenalty, Penalty presencePenalty, Penalty frequencyPenalty) {
    public AmazonBedrockJurassicRequestBody(String prompt) {
        this(prompt, 100, 0.7, 1.0, Collections.emptyList(), new Penalty(0), new Penalty(0), new Penalty(0));
    }
    public record Penalty(Integer scale) {
    }

}

