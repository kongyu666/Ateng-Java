package local.kongyu.config;

import local.kongyu.config.config.DevConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class Springboot3ConfigApplicationTests {
    private final DevConfig devConfig;

    @Test
    void contextLoads() {
        System.out.println(devConfig);

    }

}
