package local.kongyu.flink.task.sql;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 生成数据写入到kafka
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-07-19 15:33:21
 */
@Component
public class DatagenToKafka {

    //Async
    @EventListener
    public void run(ApplicationReadyEvent event) {
        // 获取环境
        StreamTableEnvironment tableEnv = SpringUtil.getBean("flinkTableEnv", StreamTableEnvironment.class);
        // 创建名为 my_user_kafka 的表，使用 DataGen connector 生成测试数据
        tableEnv.executeSql("CREATE TABLE my_user_kafka_source (\n" +
                "  id BIGINT NOT NULL,\n" +
                "  name STRING,\n" +
                "  age INT,\n" +
                "  score DOUBLE,\n" +
                "  birthday TIMESTAMP(3),\n" +
                "  province STRING,\n" +
                "  city STRING,\n" +
                "  create_time TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "  'connector' = 'datagen',\n" +
                "  'rows-per-second' = '1',\n" +
                "  'fields.id.min' = '1',\n" +
                "  'fields.id.max' = '10000',\n" +
                "  'fields.name.length' = '10',\n" +
                "  'fields.age.min' = '18',\n" +
                "  'fields.age.max' = '60',\n" +
                "  'fields.score.min' = '0',\n" +
                "  'fields.score.max' = '100',\n" +
                "  'fields.province.length' = '5',\n" +
                "  'fields.city.length' = '5'\n" +
                ");");

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

        // 插入数据
        tableEnv.executeSql("insert into my_user_kafka select * from my_user_kafka_source;");
    }
}
