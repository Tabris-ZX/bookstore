package schoolwork.bookstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private int expiration;
    private String secret;
}
