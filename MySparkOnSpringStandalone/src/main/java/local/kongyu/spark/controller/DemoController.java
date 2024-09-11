package local.kongyu.spark.controller;

import local.kongyu.spark.task.core.MySparkCoreTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * 启动Spark任务
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-07-28 19:38:35
 */
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DemoController {
    private final MySparkCoreTask mySparkCoreTask;

    private boolean JOB_DEMO_STATUS = false;

    @GetMapping("/run")
    public String run() {
        if (JOB_DEMO_STATUS) {
            return "任务正在运行中，请稍后...";
        } else {
            JOB_DEMO_STATUS = true; // 设置异步方法执行状态为true
            CompletableFuture<Void> futureResult = mySparkCoreTask.run();
            futureResult.whenComplete((result, throwable) -> {
                // 异步方法完成时的回调
                JOB_DEMO_STATUS = false; // 设置异步方法执行状态为false
            });
            return "开始运行任务...";
        }
    }
}
