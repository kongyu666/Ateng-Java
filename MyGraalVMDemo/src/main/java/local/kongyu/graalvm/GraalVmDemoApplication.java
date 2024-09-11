package local.kongyu.graalvm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class GraalVmDemoApplication {

    public static void main(String[] args) {
        // 输出 JVM 启动参数
        String javaOpts = System.getProperty("java.vm.name") + " options: " +
                System.getProperty("java.vm.info");
        System.out.println(javaOpts);
        SpringApplication.run(GraalVmDemoApplication.class, args);
    }

    @GetMapping("/demo")
    public String demo() {
        log.info("Hello World, 阿腾！");
        return "Hello World, 阿腾！";
    }

}
