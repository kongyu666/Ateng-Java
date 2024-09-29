package local.kongyu.config.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ApplicationRunner：实现此接口的类会在Spring Boot应用启动后执行其run方法。
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2024-09-29
 */
@Component
@Slf4j
public class MyApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        // 程序参数：run --name ateng --age 24
        log.info("由{}启动...", "ApplicationRunner.run()");
        List<String> name = args.getOptionValues("name");
        List<String> age = args.getOptionValues("age");
        // Options: [name, age]
        System.out.println("Options: " + args.getOptionNames());
        // Non-option args: [run, ateng, 24]
        System.out.println("Non-option args: " + args.getNonOptionArgs());
    }
}

