package cn.edu.guet.secondhandtransactionbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @Value("${file.access.url}")
    private String fileAccessUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射
        String pathPattern = fileAccessUrl + "/**";
        String location = "file:" + uploadPath + "/";
        registry.addResourceHandler(pathPattern)
                .addResourceLocations(location);
    }
}