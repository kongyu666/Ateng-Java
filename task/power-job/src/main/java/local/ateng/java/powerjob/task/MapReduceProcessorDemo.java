package local.ateng.java.powerjob.task;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.TaskResult;
import tech.powerjob.worker.core.processor.sdk.MapReduceProcessor;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 并行处理器：MapReduceProcessor
 *
 * @author 孔余
 * @since 2024-02-21 10:38
 */
@Slf4j
@Component
public class MapReduceProcessorDemo implements MapReduceProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        // 判断是否为根任务
        if (isRootTask()) {

            // 构造子任务
            List<SubTask> subTaskList = Lists.newLinkedList();

            /*
             * 子任务的构造由开发者自己定义
             * eg. 现在需要从文件中读取100W个ID，并处理数据库中这些ID对应的数据，那么步骤如下：
             * 1. 根任务（RootTask）读取文件，流式拉取100W个ID，并按1000个一批的大小组装成子任务进行派发
             * 2. 非根任务获取子任务，完成业务逻辑的处理
             */

            // 调用 map 方法，派发子任务（map 可能会失败并抛出异常，做好业务操作）
            map(subTaskList, "DATA_PROCESS_TASK");
            return new ProcessResult(true, "ROOT_PROCESS_SUCCESS");
        }

        // 非子任务，可根据 subTask 的类型 或 TaskName 来判断分支
        if (context.getSubTask() instanceof SubTask) {
            // 执行子任务，注：子任务人可以 map 产生新的子任务，可以构建任意级的 MapReduce 处理器
            return new ProcessResult(true, "PROCESS_SUB_TASK_SUCCESS");
        }

        return new ProcessResult(false, "UNKNOWN_BUG");
    }

    @Override
    public ProcessResult reduce(TaskContext taskContext, List<TaskResult> taskResults) {

        // 所有 Task 执行结束后，reduce 将会被执行
        // taskResults 保存了所有子任务的执行结果

        // 用法举例，统计执行结果
        AtomicLong successCnt = new AtomicLong(0);
        taskResults.forEach(tr -> {
            if (tr.isSuccess()) {
                successCnt.incrementAndGet();
            }
        });
        // 该结果将作为任务最终的执行结果
        return new ProcessResult(true, "success task num:" + successCnt.get());
    }

    // 自定义的子任务
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SubTask {
        private Long siteId;
        private List<Long> idList;
    }
}
