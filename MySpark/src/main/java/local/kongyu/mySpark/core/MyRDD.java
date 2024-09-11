package local.kongyu.mySpark.core;

import local.kongyu.mySpark.entity.User;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;

/**
 * 使用SparkContext的textFile方法来读取文本文件并创建JavaRDD对象，和RDD的室友
 * Spark RDD用法
 * @author 孔余
 * @since 2024-01-29 17:39
 */
public class MyRDD {
    public static void main(String[] args) {
        // 创建Spark配置
        SparkConf conf = new SparkConf();
        conf.setAppName("RDD Example");
        // 设置运行环境
        String masterValue = conf.get("spark.master", "local[*]");
        conf.setMaster(masterValue);
        // 创建Spark上下文
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 运行程序
        //textFileRDD01(sc);
        //textFileRDD02(sc);
        //textFileRDD03(sc);
        //textFileRDD03_2(sc);
        //textFileRDD04(sc);
        //textFileRDD05(sc);
        //textFileRDD06(sc);
        //textFileRDD07(sc);
        //textFileRDD08(sc);
        //textFileGroupByRDD01(sc);
        //textFileGroupByRDD02(sc);
        //textFileGroupByRDD03(sc);
        //textFileGroupByRDD04(sc);
        textFileGroupByRDD05(sc);

        // 关闭Spark上下文
        sc.close();
    }

    /**
     * 读取文本文件，打印输出
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD01(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 打印RDD中的内容
        textFileRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，过滤数据
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD02(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用filter操作，筛选出满足特定条件的数据。例如，只保留包含特定关键词的行。
        JavaRDD<String> filteredRDD = textFileRDD.filter(line -> line.contains("重庆"));

        // 打印RDD中的内容
        filteredRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，映射转换
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD03(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用map操作，对每一行数据进行转换，创建新的RDD。例如，将每一行数据按逗号分割并获取特定列的值。
        JavaRDD<String> transformedRDD = textFileRDD.map(line -> line.split(",")[2]); // 获取第三列数据

        // 打印RDD中的内容
        transformedRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，映射转换为实体类
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD03_2(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用map操作，对每一行数据进行转换，创建新的RDD。例如，将每一行数据按逗号分割并获取特定列的值。
        JavaRDD<User> transformedRDD = textFileRDD.map(User::fromCsvString); // 映射为User实体类

        // 打印RDD中的内容
        transformedRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，缓存数据和数据检查点
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD04(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用cache或persist操作，将RDD缓存到内存中，以便在迭代中复用。这对于迭代算法或多次使用相同数据的情况很有用。
        //textFileRDD.cache(); // 或 textFileRDD.persist(StorageLevel.MEMORY_ONLY());
        // 数据检查点：针对 textFileRDD 做检查点计算
        sc.setCheckpointDir("hdfs://bigdata01:8020/spark/checkpoint"); // 设置Checkpoint目录
        textFileRDD.checkpoint();

        // 打印RDD中的内容
        textFileRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，计算行数
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD05(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用count操作，获取RDD中行数。
        long count = textFileRDD.count();

        // 打印结果
        System.out.println("Total lines: " + count);
    }

    /**
     * 读取文本文件，并行处理
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD06(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将salary转换为列表数据
        List<Integer> list = textFileRDD.map(line -> Integer.parseInt(line.split(",")[0])).collect();

        // 使用parallelize方法，将集合数据转换为RDD，并进行并行处理。
        JavaRDD<Integer> parallelRDD = sc.parallelize(list);

        // 打印结果
        parallelRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，持久化输出
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD07(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用saveAsTextFile方法，将RDD保存到文本文件或其他输出格式中。
        JavaRDD<String> filteredRDD = textFileRDD.filter(line -> line.contains("重庆"));
        filteredRDD.saveAsTextFile("hdfs://bigdata01:8020/data/output/my_table_user_cq");
    }

    /**
     * 读取文本文件，合并数据
     *
     * @param sc Spark上下文
     */
    private static void textFileRDD08(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用union操作，将两个RDD合并为一个。
        JavaRDD<String> filteredRDD = textFileRDD.filter(line -> line.contains("重庆"));
        JavaRDD<String> combinedRDD = textFileRDD.union(filteredRDD);

        // 打印RDD中的内容
        combinedRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，分组操作
     *
     * @param sc Spark上下文
     */
    private static void textFileGroupByRDD01(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用groupBy操作，按照特定条件对数据进行分组。
        JavaPairRDD<String, Iterable<String>> groupedRDD = textFileRDD.groupBy(line -> line.split(",")[3]);

        // 打印RDD中的内容
        groupedRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，计数每个分组的元素个数
     *
     * @param sc Spark上下文
     */
    private static void textFileGroupByRDD02(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用groupBy操作，按照特定条件对数据进行分组。
        JavaPairRDD<String, Long> groupCountRDD = textFileRDD
                .map(User::fromCsvString)
                .groupBy(User::getRegion).mapValues(value -> value.spliterator().estimateSize());

        // 打印RDD中的内容
        groupCountRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，计算每个分组的总和
     *
     * @param sc Spark上下文
     */
    private static void textFileGroupByRDD03(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 假设每行数据包含一个数字字段，你可以使用mapToPair操作将其转换为键值对，然后使用reduceByKey操作计算每个分组的总和。
        JavaPairRDD<String, Integer> sumByGroupRDD = textFileRDD
                .mapToPair(line -> {
                    User user = User.fromCsvString(line);
                    return new Tuple2<>(user.getRegion(), user.getSalary());
                })
                .reduceByKey(Integer::sum);

        // 打印RDD中的内容
        sumByGroupRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，找到每个分组的最大值
     *
     * @param sc Spark上下文
     */
    private static void textFileGroupByRDD04(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用reduceByKey结合自定义比较器，找到每个分组的最大值。
        JavaPairRDD<String, Integer> maxByGroupRDD = textFileRDD
                .mapToPair(line -> {
                    User user = User.fromCsvString(line);
                    return new Tuple2<>(user.getRegion(), user.getSalary());
                })
                .reduceByKey(Math::max);

        // 打印RDD中的内容
        maxByGroupRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 读取文本文件，计算每个分组的平均值
     *
     * @param sc Spark上下文
     */
    private static void textFileGroupByRDD05(JavaSparkContext sc) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 使用combineByKey操作，结合自定义累加器和计数器，计算每个分组的平均值。
        JavaPairRDD<String, Double> maxByGroupRDD = textFileRDD
                .mapToPair(line -> {
                    User user = User.fromCsvString(line);
                    return new Tuple2<>(user.getRegion(), user.getSalary());
                })
                .combineByKey(
                        value -> new Tuple2<>(value, 1),
                        (accumulator, value) -> new Tuple2<>(accumulator._1() + value, accumulator._2() + 1),
                        (accumulator1, accumulator2) -> new Tuple2<>(accumulator1._1() + accumulator2._1(), accumulator1._2() + accumulator2._2())
                )
                .mapValues(accumulator -> (double) accumulator._1() / accumulator._2());

        // 打印RDD中的内容
        maxByGroupRDD.foreach(line -> System.out.println(line));
    }
}
