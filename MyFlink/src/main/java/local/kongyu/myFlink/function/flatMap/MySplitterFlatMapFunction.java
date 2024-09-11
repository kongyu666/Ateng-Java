package local.kongyu.myFlink.function.flatMap;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * FlatMap的空格分割实现类
 *
 * @author 孔余
 * @since 2024-02-29 16:13
 */
public class MySplitterFlatMapFunction implements FlatMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public void flatMap(String sentence, Collector<Tuple2<String, Integer>> collector) throws Exception {
        for (String word : sentence.split(" ")) {
            collector.collect(new Tuple2<String, Integer>(word, 1));
        }
    }
}
