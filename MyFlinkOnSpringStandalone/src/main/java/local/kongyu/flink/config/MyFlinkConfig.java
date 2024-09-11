package local.kongyu.flink.config;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flink Env
 *
 * @author 孔余
 * @since 2024-7-19 11:18:06
 */
@Configuration
public class MyFlinkConfig {
    /**
     * 执行环境
     *
     * @return flinkEnv
     */
    @Bean
    public StreamExecutionEnvironment flinkEnv() {
        return StreamExecutionEnvironment.getExecutionEnvironment();
    }

    /**
     * 流式表环境
     *
     * @param env
     * @return flinkTableEnv
     */
    @Bean
    public StreamTableEnvironment flinkTableEnv(StreamExecutionEnvironment env) {
        return StreamTableEnvironment.create(env);
    }

}
