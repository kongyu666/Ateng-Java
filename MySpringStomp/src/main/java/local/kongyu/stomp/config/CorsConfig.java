package local.kongyu.stomp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类，用于配置跨域资源共享（CORS）策略。
 * 作者：孔余
 * 日期：2024-05-11 20:56
 */
@Configuration
public class CorsConfig {

    /**
     * 配置CORS规则的方法。
     *
     * @return WebMvcConfigurer对象，用于配置CORS规则
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 添加CORS映射，允许所有来源的请求
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
