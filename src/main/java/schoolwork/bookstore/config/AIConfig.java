package schoolwork.bookstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EnableConfigurationProperties
public class AIConfig {
    private String type;
    private String header;
    private String apiKey;
    private String apiBaseUrl;
    private String model;
    private Integer timeout;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "ai.gemini")
    public static class Gemini extends AIConfig {}

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "ai.silicon-flow")
    public static class SiliconFlow extends AIConfig {}

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "ai.ark")
    public static class Ark extends AIConfig {}

}
