package local.kongyu.spark.schedule;

import local.kongyu.spark.task.core.MySparkCoreTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时运行Spark任务
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-07-28 19:47:35
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MySparkSchedule {

    private final MySparkCoreTask mySparkCoreTask;

    /**
     * 定时运行Spark任务
     */
    @Scheduled(cron = "0 * * * * ?")
    public void runSparkJob() {
        log.info("Spark任务开始运行");
        mySparkCoreTask.run();
        log.info("Spark任务运行完成");
    }
}
