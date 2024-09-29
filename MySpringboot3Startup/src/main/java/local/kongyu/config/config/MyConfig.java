package local.kongyu.config.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    /**
     * @Bean方法：定义并初始化Spring容器中的Bean。
     * @return String
     */
    @Bean
    public String myBean() {
        return "This is a bean";
    }

    /**
     * @PreDestroy：在Bean销毁前执行的方法。
     */
    @PreDestroy
    public void cleanup() {
        System.out.println("PreDestroy method executed");
    }
}

