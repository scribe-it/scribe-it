package com.message_clasificator.config;

import com.message_clasificator.ChatAnalyzer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey("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")
                .baseUrl("https://api.groq.com/openai/v1")
                .modelName("llama-3.3-70b-versatile")
                .build();
    }

    @Bean
    public ChatAnalyzer chatAnalyzer(ChatLanguageModel chatLanguageModel) {
        return AiServices.builder(ChatAnalyzer.class)
                .chatLanguageModel(chatLanguageModel)
                .build();
    }
}
