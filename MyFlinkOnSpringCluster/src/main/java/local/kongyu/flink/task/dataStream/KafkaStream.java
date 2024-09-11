package local.kongyu.flink.task.dataStream;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import local.kongyu.flink.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Apache Kafka 连接器
 * https://nightlies.apache.org/flink/flink-docs-master/zh/docs/connectors/datastream/kafka/
 * Kafka 数据流处理，用于从 Kafka 主题接收 JSON 数据，并对用户年龄进行统计。
 *
 * @author 孔余
 * @since 2024-02-29 15:59
 */
@Component
public class KafkaStream {

    /**
     * 主函数，用于执行 Kafka 数据流处理。
     */
    public void run() throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = SpringUtil.getBean("flinkEnv", StreamExecutionEnvironment.class);
        StreamTableEnvironment tableEnv = SpringUtil.getBean("flinkTableEnv", StreamTableEnvironment.class);

        // 启用检查点，设置检查点间隔为 5000 毫秒，检查点模式为 EXACTLY_ONCE
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);

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

        // 从 Kafka 数据源读取数据，设置水印策略为 BoundedOutOfOrderness，最大乱序时间为 20 秒，命名为 "Kafka Source"
        SingleOutputStreamOperator<Integer> dataStream = env
                .fromSource(source, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(20)), "Kafka Source")
                // 将 JSON 数据转换为 Java 对象，并提取用户年龄
                .map(value -> {
                    UserInfoEntity user = JSONObject.parseObject(value).toJavaObject(UserInfoEntity.class);
                    return user.getAge();
                })
                // 将所有数据按 true 键分组
                .keyBy(value -> true)
                // 使用固定大小的处理时间窗口（1分钟）进行计算
                .windowAll(TumblingProcessingTimeWindows.of(Time.minutes(1)))
                // 对所有用户年龄进行求和
                .sum(0);

        // 打印计算结果
        dataStream.print("output");

        // 执行流处理作业
        env.execute("Kafka Stream");
    }

}
