package grupo2.docubot.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import grupo2.docubot.repository.ChatAnalyzer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    @Value("${groq.api.key}")
    private String apiKey;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
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
