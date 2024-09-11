package local.kongyu.myFlink.dataStream.transformations;

import local.kongyu.myFlink.entity.UserInfoEntity;
import local.kongyu.myFlink.function.filter.MyFilter01;
import local.kongyu.myFlink.function.flatMap.MyFlatMapFunction01;
import local.kongyu.myFlink.function.map.MyMapFunction01;
import local.kongyu.myFlink.function.map.MyMapFunction02;
import local.kongyu.myFlink.function.map.MyMapFunction03;
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
 * 数据流转换
 * https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/operators/overview/
 *
 * @author 孔余
 * @since 2024-02-29 15:59
 */
public class DataStreamTransformations {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
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

        // 从 Kafka 数据源读取数据，设置水印策略为 BoundedOutOfOrderness，最大乱序时间为 3 秒，命名为 "Kafka Source"
        DataStreamSource<String> streamSource = env.fromSource(source, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)), "Kafka Source");

        // 执行程序
//        map01(streamSource);
//        flatMap01(streamSource);
//        filter01(streamSource);
//        keyByAndReduce01(streamSource);
//        keyByAndReduce02(streamSource);
//        keyByAndReduce03(streamSource);
        window01(streamSource);

        // 执行流处理作业
        env.execute("Kafka Stream");
    }

    /**
     * Map
     * DataStream → DataStream
     */
    private static void map01(DataStreamSource<String> streamSource) {
        SingleOutputStreamOperator<UserInfoEntity> operator = streamSource
                .map(new MyMapFunction01());
        operator.print();
    }

    /**
     * FlatMap
     * DataStream → DataStream
     */
    private static void flatMap01(DataStreamSource<String> streamSource) {
        SingleOutputStreamOperator<UserInfoEntity> operator = streamSource
                .flatMap(new MyFlatMapFunction01());
        operator.print();
    }

    /**
     * Filter
     * DataStream → DataStream
     */
    private static void filter01(DataStreamSource<String> streamSource) {
        SingleOutputStreamOperator<String> operator = streamSource
                .filter(new MyFilter01());
        operator.print();
    }

    /**
     * KeyBy和Reduce
     * DataStream → KeyedStream → DataStream
     * 逻辑上将流划分为不相交的分区。所有具有相同键的记录都被分配到同一个分区。在内部，keyBy()是通过散列分区实现的。有不同的方法来指定键。
     * 键控数据流上的“滚动”约简。将当前元素与最后一个简化值合并，并发出新值。创建部分和流的reduce函数:
     */
    private static void keyByAndReduce01(DataStreamSource<String> streamSource) {
        SingleOutputStreamOperator<Tuple2<String, Long>> operator = streamSource
                .map(new MyMapFunction02())
                // 按区域进行分区
                .keyBy(key -> key.f0)
                // 对相同键的元素进行累加
                .reduce((data1, data2) -> new Tuple2<>(data1.f0, data1.f1 + data2.f1));
        operator.print();
    }

    /**
     * 求每个区域分数的最大值
     * @param streamSource
     */
    private static void keyByAndReduce02(DataStreamSource<String> streamSource) {
        streamSource
                .map(new MyMapFunction03())
                .keyBy(key -> key.f0)
                .max(1)
                .print();
    }

    /**
     * 求每个区域分数的和
     * @param streamSource
     */
    private static void keyByAndReduce03(DataStreamSource<String> streamSource) {
        streamSource
                .map(new MyMapFunction03())
                .keyBy(key -> key.f0)
                .sum(1)
                .print();
    }

    /**
     * Window
     * DataStream → KeyedStream → WindowedStream → DataStream
     * Windows可以在已经分区的KeyedStreams上定义。Windows根据某些特征(例如，最近5秒内到达的数据)将每个键中的数据分组。
     * @param streamSource
     */
    private static void window01(DataStreamSource<String> streamSource) {
        SingleOutputStreamOperator<Tuple2<String, Long>> operator = streamSource
                .map(new MyMapFunction02())
                .keyBy(key -> key.f0)
                // 划分为 10 秒的滚动时间窗口，并对窗口内的数据进行求和操作
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
//                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .sum(1);
        operator.print();

    }

}
