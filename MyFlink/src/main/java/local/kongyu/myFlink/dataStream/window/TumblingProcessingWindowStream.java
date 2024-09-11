package local.kongyu.myFlink.dataStream.window;

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
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.time.Duration;

/**
 * 演示 Flink 中的基于滚动处理时间窗口的流处理。
 */
public class TumblingProcessingWindowStream {

    /**
     * 主函数，用于执行基于滚动处理时间窗口的流处理。
     * @param args 命令行参数
     * @throws Exception 可能抛出的异常
     */
    public static void main(String[] args) throws Exception {

        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 启用检查点，设置检查点间隔为 5000 毫秒，检查点模式为 EXACTLY_ONCE
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);

        // 创建 Kafka 数据源，连接到指定的 Kafka 服务器和主题
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

        // 从 Kafka 数据源读取数据，设置水印策略为 BoundedOutOfOrderness，最大乱序时间为 10 秒，命名为 "Kafka Source"
        DataStreamSource<String> streamSource = env
                .fromSource(source, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10)), "Kafka Source");

        // 对数据流进行处理：映射为元组、按键分区、应用基于滚动处理时间窗口的求和操作
        SingleOutputStreamOperator<Tuple2<String, Long>> operator = streamSource
                .map(new MyMapFunction02()) // 将输入的字符串转换为元组
                .keyBy(key -> key.f0) // 按照元组的第一个元素进行分区
                .window(TumblingProcessingTimeWindows.of(Time.minutes(2))) // 划分为 2 分钟的滚动处理时间窗口
                .reduce((value1, value2) -> new Tuple2<String, Long>(value1.f0, value1.f1 + value2.f1)); // 对窗口内的元素进行求和操作

        // 打印处理结果
        operator.print();

        // 执行流处理作业
        env.execute();
    }
}

