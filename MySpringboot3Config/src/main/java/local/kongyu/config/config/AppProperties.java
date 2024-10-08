package local.kongyu.config.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "app")
@Configuration
@Data
public class AppProperties {
    private String name;
    private int port;
    private List<Integer> ids;
    private Ateng ateng;

    @Data
    public static class Ateng{
        private String name;
        private int age;
    }
}
