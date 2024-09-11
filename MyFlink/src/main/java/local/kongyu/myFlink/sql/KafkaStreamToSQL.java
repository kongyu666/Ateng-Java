package local.kongyu.myFlink.sql;

import local.kongyu.myFlink.entity.UserEntity;
import local.kongyu.myFlink.function.map.MyMapFunction011;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.time.Duration;

/**
 * 将Kafka Stream的数据转换成SQL并处理
 *
 * @author 孔余
 * @since 2024-03-07 14:28:42
 */
public class KafkaStreamToSQL {

    public static void main(String[] args) throws Exception {

        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 创建流式表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 启用检查点，设置检查点间隔为 1000 毫秒，检查点模式为 EXACTLY_ONCE
        env.enableCheckpointing(1000, CheckpointingMode.EXACTLY_ONCE);

        // 创建 Kafka 数据源，连接到指定的 Kafka 服务器和主题
        KafkaSource<String> source = KafkaSource.<String>builder()
                // 设置 Kafka 服务器地址
                .setBootstrapServers("192.168.1.10:9094")
                // 设置要订阅的主题
                .setTopics("ateng_flink_json")
                // 设置消费者组 ID
                .setGroupId("ateng")
                // 设置在检查点时提交偏移量（offsets）以确保精确一次语义
                .setProperty("commit.offsets.on.checkpoint", "true")
                // 启用自动提交偏移量
                .setProperty("enable.auto.commit", "true")
                // 自动提交偏移量的时间间隔
                .setProperty("auto.commit.interval.ms", "1000")
                // 设置分区发现的时间间隔
                .setProperty("partition.discovery.interval.ms", "10000")
                // 设置起始偏移量（如果没有提交的偏移量，则从最早的偏移量开始消费）
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                // 设置仅接收值的反序列化器
                .setValueOnlyDeserializer(new SimpleStringSchema())
                // 构建 Kafka 数据源
                .build();

        // 从 Kafka 数据源读取数据，命名为 "Kafka Source"
        SingleOutputStreamOperator<UserEntity> dataStream = env
                .fromSource(source, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(20)), "Kafka Source")
                // 将 JSON 数据转换为 Java 对象
                .map(new MyMapFunction011());

        // 将数据流转换为表
        Table userTable = tableEnv.fromDataStream(dataStream);
        // 在表环境中创建临时视图
        tableEnv.createTemporaryView("my_user", userTable);

        /*
           可选：根据条件过滤数据表
           Table filterTable = tableEnv.sqlQuery("select * from my_user where age > 24");
           filterTable.execute().print();
        */

        // 根据省份分组计算分数总和
        Table groupTable = tableEnv.sqlQuery("select province, sum(score) as score_sum from my_user group by province");
        // 执行并打印分组结果表
        groupTable.execute().print();

        // 执行流处理作业
        env.execute("KafkaStreamToSQL");
    }

}
