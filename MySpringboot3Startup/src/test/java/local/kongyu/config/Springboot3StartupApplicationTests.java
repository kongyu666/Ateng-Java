package local.kongyu.config;

import local.kongyu.config.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class Springboot3StartupApplicationTests {
    private final AppProperties appProperties;

    @Test
    void contextLoads() {
        System.out.println(appProperties);

    }

}
