package local.kongyu.myFlink.dataStream.window;

import com.alibaba.fastjson2.JSONObject;
import local.kongyu.myFlink.entity.UserInfoEntity;
import local.kongyu.myFlink.function.map.MyMapFunction02;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.time.Duration;

/**
 * 演示 Flink 中的基于滚动事件时间窗口的流处理。
 */
public class TumblingEventWindowStream {

    public static void main(String[] args) throws Exception {

        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 启用精确一次的检查点
        env.enableCheckpointing(1000, CheckpointingMode.EXACTLY_ONCE);
        // Kafka的Topic有多少个分区就设置多少并行度，例如：Topic有3个分区就设置并行度为3
        env.setParallelism(3);

        // KafkaSource 配置
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.1.10:9094")
                .setTopics("ateng_flink_json")
                .setGroupId("ateng")
                .setProperty("commit.offsets.on.checkpoint", "true")
                .setProperty("enable.auto.commit", "true")
                .setProperty("auto.commit.interval.ms", "1000")
                .setProperty("partition.discovery.interval.ms", "10000")
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        // 定义水印策略
        WatermarkStrategy<String> watermarkStrategy = WatermarkStrategy.<String>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner(
                        (event, recordTimestamp) -> {
                            // 解析 JSON 格式的事件，并获取事件时间
                            UserInfoEntity user = JSONObject.parseObject(event).toJavaObject(UserInfoEntity.class);
                            long time = user.getCreateTime().getTime();
                            return time;
                        });

        // 从 Kafka 中读取数据流
        DataStreamSource<String> streamSource = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        // 应用水印策略，并进行窗口操作
        SingleOutputStreamOperator<Tuple2<String, Long>> operator = streamSource
                .assignTimestampsAndWatermarks(watermarkStrategy)
                .map(new MyMapFunction02())  // 对事件进行转换
                .keyBy(key -> key.f0)  // 根据键分组
                .window(TumblingEventTimeWindows.of(Time.minutes(2)))  // 定义2分钟的事件时间窗口
                .reduce((value1, value2) -> new Tuple2<String, Long>(value1.f0, value1.f1 + value2.f1));  // 对窗口内数据进行汇总

        // 打印结果
        operator.print();

        // 执行作业
        env.execute();
    }

}
