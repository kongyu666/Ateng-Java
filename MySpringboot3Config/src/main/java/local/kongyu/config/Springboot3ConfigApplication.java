package local.kongyu.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Springboot3ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot3ConfigApplication.class, args);
    }

}
