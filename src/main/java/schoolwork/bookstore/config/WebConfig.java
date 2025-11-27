package schoolwork.bookstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${path.data-dir}")
    private String dataDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")        // 所有接口
                .allowedOriginPatterns("*")    // 允许所有域
                .allowedMethods("GET","POST","PUT","DELETE") // 允许方法
//                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置静态资源映射
     * 将URL路径映射到文件系统路径，使题解文件可以被访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射题解文件目录
        registry.addResourceHandler("/solutions/**")
                .addResourceLocations("file:" + dataDir + "/solutions/");

        // 映射待审核题解目录
        registry.addResourceHandler("/pending-solutions/**")
                .addResourceLocations("file:" + dataDir + "/pending-solutions/");

        // 映射用户文件目录
        registry.addResourceHandler("/users/**")
                .addResourceLocations("file:" + dataDir + "/users/");
    }
}