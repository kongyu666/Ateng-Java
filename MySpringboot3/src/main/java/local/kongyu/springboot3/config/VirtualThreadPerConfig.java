package local.kongyu.springboot3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

/**
 * 开启虚拟线程
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-06-24 13:50:47
 */
@Configuration
public class VirtualThreadPerConfig {

    /**
     * 异步任务执行器开启虚拟线程
     *
     * @Async
     */
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * Tomcat Web开启虚拟线程，Undertow Web自带虚拟线程（>=3.3.0）
     */
    /*@Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }*/
}
