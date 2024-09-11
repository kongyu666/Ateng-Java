package local.kongyu.mySpark.core;

import local.kongyu.mySpark.entity.User;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;


/**
 * 创建Spark上下文和Spark Session，对Dataset的使用
 * Spark Dataset用法
 * @author 孔余
 * @since 2024-01-30 18:14
 */
public class MyDataset {
    public static void main(String[] args) {
        // 创建Spark配置
        SparkConf conf = new SparkConf();
        conf.setAppName("Dataset Example");
        // 设置运行环境
        String masterValue = conf.get("spark.master", "local[*]");
        conf.setMaster(masterValue);
        // 创建Spark上下文
        JavaSparkContext sc = new JavaSparkContext(conf);
        // 创建Spark Session
        SparkSession spark = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        // 运行程序
        //dataset01(sc, spark);
        //dataset02(sc, spark);
        //dataset03(sc, spark);
        //dataset04(sc, spark);
        //dataset05(sc, spark);
        //dataset06(sc, spark);
        dataset07(sc, spark);

        // 关闭Spark上下文
        sc.stop();
        // 关闭Spark Session
        spark.stop();
    }

    /**
     * RDD转Dataset
     *
     * @param sc    Spark上下文
     * @param spark Spark Session
     */
    private static void dataset01(JavaSparkContext sc, SparkSession spark) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将RDD中的数据映射到Row对象
        JavaRDD<User> userRDD = textFileRDD.map(User::fromCsvString);

        // 创建Dataset
        Dataset<Row> ds = spark.createDataFrame(userRDD, User.class);

        // 显示Dataset的内容
        ds.show();
    }

    /**
     * Dataset转RDD
     *
     * @param sc    Spark上下文
     * @param spark Spark Session
     */
    private static void dataset02(JavaSparkContext sc, SparkSession spark) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将RDD中的数据映射到Row对象
        JavaRDD<User> userRDD = textFileRDD.map(User::fromCsvString);

        // 创建Dataset
        Dataset<Row> ds = spark.createDataFrame(userRDD, User.class);

        JavaRDD<Row> javaRDD = ds.toJavaRDD();

        // 输出javaRDD
        javaRDD.foreach(line -> System.out.println(line));
    }

    /**
     * 选择列
     *
     * @param sc    Spark上下文
     * @param spark Spark Session
     */
    private static void dataset03(JavaSparkContext sc, SparkSession spark) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将RDD中的数据映射到Row对象
        JavaRDD<User> userRDD = textFileRDD.map(User::fromCsvString);

        // 创建Dataset
        Dataset<Row> ds = spark.createDataFrame(userRDD, User.class);

        // 选择指定列
        Dataset<Row> selectedColumns = ds.select("name", "region");

        // 显示Dataset的内容
        selectedColumns.show();
    }

    /**
     * 过滤数据
     *
     * @param sc    Spark上下文
     * @param spark Spark Session
     */
    private static void dataset04(JavaSparkContext sc, SparkSession spark) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将RDD中的数据映射到Row对象
        JavaRDD<User> userRDD = textFileRDD.map(User::fromCsvString);

        // 创建Dataset
        Dataset<Row> ds = spark.createDataFrame(userRDD, User.class);

        // 过滤数据
        Dataset<Row> filteredDataset = ds.filter("age = 24");

        // 显示Dataset的内容
        filteredDataset.show();
    }

    /**
     * 分组和聚合
     *
     * @param sc    Spark上下文
     * @param spark Spark Session
     */
    private static void dataset05(JavaSparkContext sc, SparkSession spark) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将RDD中的数据映射到Row对象
        JavaRDD<User> userRDD = textFileRDD.map(User::fromCsvString);

        // 创建Dataset
        Dataset<Row> ds = spark.createDataFrame(userRDD, User.class);

        // 分组和聚合
        Dataset<Row> groupedData = ds.groupBy("region").agg(functions.avg("age").as("avg_age"));

        // 显示Dataset的内容
        groupedData.show();
    }

    /**
     * 排序
     *
     * @param sc    Spark上下文
     * @param spark Spark Session
     */
    private static void dataset06(JavaSparkContext sc, SparkSession spark) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将RDD中的数据映射到Row对象
        JavaRDD<User> userRDD = textFileRDD.map(User::fromCsvString);

        // 创建Dataset
        Dataset<Row> ds = spark.createDataFrame(userRDD, User.class);

        // 排序
        Dataset<Row> sortedData = ds.orderBy("salary");

        // 显示Dataset的内容
        sortedData.show();
    }

    /**
     * 使用 SQL 表达式
     *
     * @param sc    Spark上下文
     * @param spark Spark Session
     */
    private static void dataset07(JavaSparkContext sc, SparkSession spark) {
        // 读取文本文件，创建JavaRDD
        JavaRDD<String> textFileRDD = sc.textFile("hdfs://bigdata01:8020/data/my_table_user.csv");

        // 将RDD中的数据映射到Row对象
        JavaRDD<User> userRDD = textFileRDD.map(User::fromCsvString);

        // 创建Dataset
        Dataset<Row> ds = spark.createDataFrame(userRDD, User.class);

        // 使用 SQL 表达式
        Dataset<Row> result = ds.selectExpr("name", "age * 2 as double_age");

        // 显示Dataset的内容
        result.show();
    }

}
