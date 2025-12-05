package schoolwork.bookstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import schoolwork.bookstore.auth.interceptor.AdminInterceptor;
import schoolwork.bookstore.auth.interceptor.GlobalInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${path.image-dir}")
    private String imageDir;

    GlobalInterceptor globalInterceptor;
    AdminInterceptor adminInterceptor;
    public WebConfig(GlobalInterceptor globalInterceptor, AdminInterceptor adminInterceptor) {
        this.globalInterceptor = globalInterceptor;
        this.adminInterceptor  = adminInterceptor;
    }
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor).addPathPatterns("/**").excludePathPatterns("/api/users/register","/api/users/login","/api/books/**");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/api/admin/**");
    }

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
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射图书封面图片
        registry.addResourceHandler("/api/books/covers/**")
                .addResourceLocations("file:" + imageDir + "/bookCovers/");
    }
}