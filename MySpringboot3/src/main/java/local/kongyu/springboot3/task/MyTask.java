package local.kongyu.springboot3.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-06-24 13:46:20
 */
@Component
@Slf4j
public class MyTask {

    @Scheduled(cron = "0/10 * * * * ?")
    @Async
    public void task1() {
        log.info("你好啊，task1");
    }
}
