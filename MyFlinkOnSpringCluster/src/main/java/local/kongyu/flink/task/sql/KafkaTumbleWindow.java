package local.kongyu.flink.task.sql;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.stereotype.Component;

/**
 * 生成数据写入到kafka
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-07-19 15:33:21
 */
@Component
public class KafkaTumbleWindow {
    public void run() {
        // 获取环境
        StreamExecutionEnvironment env = SpringUtil.getBean("flinkEnv", StreamExecutionEnvironment.class);
        StreamTableEnvironment tableEnv = SpringUtil.getBean("flinkTableEnv", StreamTableEnvironment.class);
        ExecutionConfig config = env.getConfig();
        // 创建名为 my_user_kafka 的表，使用 Kafka connector 写入数据到 Kafka
        String kafkaServers = SpringUtil.getProperty("flink.kafka.servers");
        tableEnv.executeSql("CREATE TABLE my_user_kafka( \n" +
                "  my_event_time TIMESTAMP(3) METADATA FROM 'timestamp' VIRTUAL,\n" +
                "  my_partition BIGINT METADATA FROM 'partition' VIRTUAL,\n" +
                "  my_offset BIGINT METADATA FROM 'offset' VIRTUAL,\n" +
                "  id BIGINT NOT NULL,\n" +
                "  name STRING,\n" +
                "  age INT,\n" +
                "  score DOUBLE,\n" +
                "  birthday TIMESTAMP(3),\n" +
                "  province STRING,\n" +
                "  city STRING,\n" +
                "  create_time TIMESTAMP(3)\n" +
                ")\n" +
                "WITH (\n" +
                "  'connector' = 'kafka',\n" +
                "  'properties.bootstrap.servers' = '" + kafkaServers + "',\n" +
                "  'properties.group.id' = 'ateng_sql',\n" +
                "  -- 'earliest-offset', 'latest-offset', 'group-offsets', 'timestamp' and 'specific-offsets'\n" +
                "  'scan.startup.mode' = 'group-offsets',\n" +
                "  'topic' = 'ateng_flink_json',\n" +
                "  'format' = 'json'\n" +
                ");");

        // 执行 SQL 查询获取表数据
        Table table = tableEnv.sqlQuery("select * from my_user_kafka");
        // 执行表查询并打印结果
        TableResult result = table.execute();
        result.print();
    }
}
