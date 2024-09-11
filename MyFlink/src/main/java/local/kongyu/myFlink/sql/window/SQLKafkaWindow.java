package local.kongyu.myFlink.sql.window;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * 创建Kafka表并生成数据
 *
 * @author 孔余
 * @since 2024-03-06 17:19
 */
public class SQLKafkaWindow {
    public static void main(String[] args) throws Exception {
        // 获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 创建流式表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // Kafka的Topic有多少个分区就设置多少并行度，例如：Topic有3个分区就设置并行度为3
        env.setParallelism(3);

        // 创建名为 my_user 的表，并设置水位线
        tableEnv.executeSql("CREATE TABLE my_user_window_kafka (\n" +
                "  eventTime TIMESTAMP(3) METADATA FROM 'timestamp' VIRTUAL,\n" +
                "  my_partition BIGINT METADATA FROM 'partition' VIRTUAL,\n" +
                "  my_offset BIGINT METADATA FROM 'offset' VIRTUAL,\n" +
                "  id BIGINT NOT NULL,\n" +
                "  name STRING,\n" +
                "  age INT,\n" +
                "  score DOUBLE,\n" +
                "  birthday TIMESTAMP(3),\n" +
                "  province STRING,\n" +
                "  city STRING,\n" +
                "  createTime TIMESTAMP(3),\n" +
                "  WATERMARK FOR createTime AS createTime - INTERVAL '5' SECOND\n" +
                ") WITH (\n" +
                "  'connector' = 'kafka',\n" +
                "  'topic' = 'ateng_flink_json',\n" +
                "  'properties.group.id' = 'ateng_sql_window',\n" +
                "  'properties.enable.auto.commit' = 'true',\n" +
                "  'properties.auto.commit.interval.ms' = '1000',\n" +
                "  'properties.partition.discovery.interval.ms' = '10000',\n" +
                "  -- 'earliest-offset', 'latest-offset', 'group-offsets', 'timestamp' and 'specific-offsets'\n" +
                "  'scan.startup.mode' = 'group-offsets',\n" +
                "  'properties.bootstrap.servers' = '192.168.1.10:9094',\n" +
                "  'format' = 'json'\n" +
                ");");

        // 执行 SQL 查询获取表数据: 事件时间滚动窗口(2分钟)查询
        Table table = tableEnv.sqlQuery("SELECT\n" +
                "  province,\n" +
                "  COUNT(name) AS cnt,\n" +
                "  TUMBLE_START(createTime, INTERVAL '2' MINUTE) AS window_start,\n" +
                "  TUMBLE_END(createTime, INTERVAL '2' MINUTE) AS window_end\n" +
                "FROM my_user_window_kafka\n" +
                "GROUP BY\n" +
                "  province,\n" +
                "  TUMBLE(createTime, INTERVAL '2' MINUTE);");

        // 执行表查询并打印结果
        TableResult result = table.execute();
        result.print();
    }
}
