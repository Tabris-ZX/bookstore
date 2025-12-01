package schoolwork.bookstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Data
    @ConfigurationProperties(prefix = "ai.gemini")
    public static class Gemini {
        String apiKey;
        String apiBaseUrl;
        String model;
        Integer timeout;
    }

    @Data
    @ConfigurationProperties(prefix = "ai.silicon-flow")
    public static class SiliconFlow {
        private String apiKey;
        private String apiBaseUrl;
        private  String model;
        private Integer timeout;
    }
}
