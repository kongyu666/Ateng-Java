package local.kongyu.myFlink.dataStream.basic;

import local.kongyu.myFlink.function.flatMap.MySplitterFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 套接字文本流处理，用于从套接字接收文本数据，并对单词进行统计。
 * 如下是一个完整的、可运行的程序示例，它是基于流窗口的单词统计应用程序，计算 5 秒窗口内来自 Web 套接字的单词数。
 *
 * @author 孔余
 * @since 2024-02-29 15:59
 */
public class SocketTextStream {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 从指定套接字地址和端口接收文本数据
        DataStream<Tuple2<String, Integer>> dataStream = env
                .socketTextStream("bigdata01", 9999)
                // 使用 MySplitterFlatMapFunction 对文本进行拆分，并计算单词频率
                .flatMap(new MySplitterFlatMapFunction())
                // 按单词进行分组
                .keyBy(value -> value.f0)
                // 使用固定大小的处理时间窗口（5秒）进行计算
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                // 对相同单词的计数进行求和
                .sum(1);

        // 打印计算结果
        dataStream.print();

        // 执行流处理作业
        env.execute("Window WordCount");
    }

}
