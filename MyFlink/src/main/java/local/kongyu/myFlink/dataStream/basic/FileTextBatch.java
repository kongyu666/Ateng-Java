package local.kongyu.myFlink.dataStream.basic;

import local.kongyu.myFlink.function.flatMap.MySplitterFlatMapFunction;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 文件文本批处理，用于从文件中读取文本数据，并对单词进行统计。
 *
 * @author 孔余
 * @since 2024-02-29 15:59
 */
public class FileTextBatch {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置运行模式为批处理模式
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

        // 创建文件源，从指定路径读取文本数据
        FileSource<String> source = FileSource.forRecordStreamFormat(
                new TextLineInputFormat(),
                new Path("hdfs://bigdata01:8020/data/flink/word.txt")
        ).build();

        // 从文件源读取数据，不生成水印，命名为 "TextSource"
        SingleOutputStreamOperator<Tuple2<String, Integer>> dataStream = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "TextSource")
                // 使用 MySplitterFlatMapFunction 对文本进行拆分，并计算单词频率
                .flatMap(new MySplitterFlatMapFunction())
                // 按单词进行分组
                .keyBy(value -> value.f0)
                // 对相同单词的计数进行求和
                .sum(1);

        // 打印计算结果
        dataStream.print();

        // 执行批处理作业
        env.execute("File WordCount");
    }

}