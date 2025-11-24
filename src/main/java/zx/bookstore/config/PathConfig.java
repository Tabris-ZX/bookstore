package zx.bookstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "path")
public class PathConfig {
    private String dataDir;

    private String pstDir;
    private String imageDir;

    private String tmpDir;
    private String uploadDir;
    private String logDir;

}
