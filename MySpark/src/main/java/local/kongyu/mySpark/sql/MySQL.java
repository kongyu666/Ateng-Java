package local.kongyu.mySpark.sql;

import cn.hutool.core.util.StrUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * Spark SQL用法
 *
 * @author 孔余
 * @since 2024-01-30 22:17
 */
public class MySQL {
    public static void main(String[] args) {
        // 创建Spark配置
        SparkConf conf = new SparkConf();
        // 设置应用程序的名称
        conf.setAppName("SQL Example");
        // 指定hive仓库中的默认位置
        conf.set("spark.sql.warehouse.dir", "hdfs://bigdata01:8020/hive/warehouse");
        // 设置运行环境
        String masterValue = conf.get("spark.master", "local[*]");
        conf.setMaster(masterValue);
        // 创建一个SparkSession对象，同时配置SparkConf，并启用Hive支持
        SparkSession spark = SparkSession
                .builder()
                .config(conf)
                .enableHiveSupport()
                .getOrCreate();

        // 执行程序
        //sql01(spark);
        //sql02(spark);
        //sql03(spark);
        //sql03_2(spark);
        //sql04(spark);
        sql05(spark);

        // 停止SparkSession，释放资源
        spark.stop();
    }

    /**
     * 执行第一个SQL
     *
     * @param spark SparkSession
     */
    private static void sql01(SparkSession spark) {
        // 执行SQL查询
        Dataset<Row> ds = spark.sql("select * from user");

        // 显示 DataFrame 的结构
        ds.printSchema();

        // 显示查询结果
        ds.show();
    }

    /**
     * 动态传参执行SQL
     *
     * @param spark SparkSession
     */
    private static void sql02(SparkSession spark) {
        // SQL
        String sql = "select * from user where age = {}";

        // 执行SQL查询
        Dataset<Row> ds = spark.sql(StrUtil.format(sql, 24));

        // 显示查询结果
        ds.show();
    }

    /**
     * 导出数据到MySQL
     *
     * @param spark SparkSession
     */
    private static void sql03(SparkSession spark) {
        // SQL
        String sql = "select * from user where age = {} and date_time > '{}'";

        // 从Spark SQL中获取数据集
        Dataset<Row> ds = spark.sql(StrUtil.format(sql, 24, "1970-01-01 00:00:01"));

        // 设置MySQL连接属性
        String jdbcUrl = "jdbc:mysql://192.168.1.10:35725/kongyu";
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "Admin@123");

        // 将数据写入MySQL
        ds.write()
                .mode(SaveMode.Overwrite) // 可根据需求选择保存模式，这里选择覆盖已存在的表
                .jdbc(jdbcUrl, "user", connectionProperties);

    }
    /**
     * 导出数据到PostgreSQL
     *
     * @param spark SparkSession
     */
    private static void sql03_2(SparkSession spark) {
        // SQL
        String sql = "select * from user where age = {} and date_time > '{}'";

        // 从Spark SQL中获取数据集
        Dataset<Row> ds = spark.sql(StrUtil.format(sql, 24, "1970-01-01 00:00:01"));

        // 设置PostgreSQL连接属性
        String jdbcUrl = "jdbc:postgresql://192.168.1.10:32297/kongyu?currentSchema=public";
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "postgres");
        connectionProperties.put("password", "Lingo@local_postgresql_5432");

        // 将数据写入PostgreSQL
        ds.write()
                .mode(SaveMode.Overwrite) // 可根据需求选择保存模式，这里选择覆盖已存在的表
                .jdbc(jdbcUrl, "user_spark", connectionProperties);

    }

    /**
     * 读取MySQL的数据
     *
     * @param spark SparkSession
     */
    private static void sql04(SparkSession spark) {
        // 设置MySQL连接属性
        String jdbcUrl = "jdbc:mysql://192.168.1.10:35725/kongyu";
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "Admin@123");

        // 读取MySQL的数据
        Dataset<Row> mysqlUser = spark.read().jdbc(jdbcUrl, "user", connectionProperties);

        // 输出内容
        mysqlUser.show();
    }

    /**
     * 将Dataset数据保存到Hive表中
     *
     * @param spark SparkSession
     */
    private static void sql05(SparkSession spark) {
        // 设置MySQL连接属性
        String jdbcUrl = "jdbc:mysql://192.168.1.10:35725/kongyu";
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "Admin@123");

        // 读取MySQL的数据
        Dataset<Row> mysqlUser = spark.read().jdbc(jdbcUrl, "( SELECT * FROM user WHERE age = 10 ) AS tmp", connectionProperties);

        // 将Dataset数据保存到hive中
        mysqlUser
                .write()
                .mode(SaveMode.Overwrite)
                .saveAsTable("mysql_user2");

    }
}
