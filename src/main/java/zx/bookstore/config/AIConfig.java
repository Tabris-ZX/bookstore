package zx.bookstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



public class AIConfig {

    @Data
    @ConfigurationProperties(prefix = "ai.openai")
    public static class OpenAI {
        String apiKey;
        String apiBaseUrl;
        Integer timeout;
    }

    @Data
    @ConfigurationProperties(prefix = "ai.silicon-flow")
    public static class SiliconFlow {
        private String apiKey;
        private String apiBaseUrl;
        private Integer timeout;
    }
}
