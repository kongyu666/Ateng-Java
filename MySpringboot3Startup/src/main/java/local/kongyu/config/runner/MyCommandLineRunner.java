package local.kongyu.config.runner;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CommandLineRunner：类似于ApplicationRunner，但接收命令行参数。
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2024-09-29
 */
@Component
@Slf4j
public class MyCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        // 程序参数：run --name ateng --age 24
        List<String> argList = CollUtil.newArrayList(args);
        log.info("由{}启动...", "CommandLineRunner.run()");
        // Received command line args: [run, --name, ateng, --age, 24]
        System.out.println("Received command line args: " + argList);
    }
}
