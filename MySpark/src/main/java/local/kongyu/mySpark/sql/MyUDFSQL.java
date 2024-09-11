package local.kongyu.mySpark.sql;

import local.kongyu.mySpark.udf.StringLengthUDF;
import local.kongyu.mySpark.udf.SumUDF;
import local.kongyu.mySpark.udf.UpperCaseUDF;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-31 21:14
 */
public class MyUDFSQL {
    public static void main(String[] args) {
        // 创建Spark配置
        SparkConf conf = new SparkConf();
        // 设置应用程序的名称
        conf.setAppName("UDF Example");
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
        sql03(spark);

        // 停止SparkSession，释放资源
        spark.stop();
    }

    /**
     * 小写转大写UDF
     * 一列数据
     *
     * @param spark SparkSession
     */
    private static void sql01(SparkSession spark) {
        // 注册 UDF
        spark.udf().register("upperCaseUDF", UpperCaseUDF.upperCaseUDF());

        // 使用 UDF 进行转换
        Dataset<Row> ds = spark.sql("select uuid,name,upperCaseUDF(email) as newColumn from user");

        // 显示查询结果
        ds.show();
    }

    /**
     * 数据长度
     * 一列数据
     *
     * @param spark SparkSession
     */
    private static void sql02(SparkSession spark) {
        // 注册 UDF
        spark.udf().register("stringLengthUDF", StringLengthUDF.stringLengthUDF());

        // 使用 UDF 进行转换
        Dataset<Row> ds = spark.sql("select uuid,name,stringLengthUDF(email) as newColumn from user");

        // 显示查询结果
        ds.show();
    }

    /**
     * 计算两列Integer数据的合
     * 两列数据
     * @param spark SparkSession
     */
    private static void sql03(SparkSession spark) {
        // 注册 UDF
        spark.udf().register("sumUDF", SumUDF.sumUDF());

        // 使用 UDF 进行转换
        Dataset<Row> ds = spark.sql("select name,sumUDF(age,salary) as newColumn from user");

        // 显示查询结果
        ds.show();
    }

}
