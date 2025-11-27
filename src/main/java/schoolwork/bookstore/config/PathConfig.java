package schoolwork.bookstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "path")
public class PathConfig {
    private String dataDir;

    private String pstDir;
    private String imageDir;

    private String tmpDir;
    private String uploadDir;
    private String logDir;

}
