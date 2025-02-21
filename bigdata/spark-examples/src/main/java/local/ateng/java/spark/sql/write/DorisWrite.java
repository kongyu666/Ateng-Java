package local.ateng.java.spark.sql.write;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

/**
 * 将查询的数据输出到Doris
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-24
 */
public class DorisWrite {
    public static void main(String[] args) {
        // 创建Spark配置
        SparkConf conf = new SparkConf();
        // 设置应用程序的名称
        conf.setAppName("将查询的数据输出到Doris");
        // 指定hive仓库中的默认位置
        conf.set("spark.sql.warehouse.dir", "hdfs://server01:8020/hive/warehouse");
        // 设置运行环境
        String masterValue = conf.get("spark.master", "local[*]");
        conf.setMaster(masterValue);
        // 创建一个SparkSession对象，同时配置SparkConf，并启用Hive支持
        SparkSession spark = SparkSession
                .builder()
                .config(conf)
                .enableHiveSupport()
                .getOrCreate();

        // 创建Doris视图
        spark.sql("CREATE TEMPORARY VIEW spark_doris\n" +
                "   USING doris\n" +
                "   OPTIONS(\n" +
                "   \"table.identifier\"=\"kongyu.sink_my_user_spark\",\n" +
                "   \"fenodes\"=\"192.168.1.12:9040\",\n" +
                "   \"user\"=\"admin\",\n" +
                "   \"password\"=\"Admin@123\"\n" +
                ");");

        // 写入数据到Doris，insert into select | insert overwrite select
        String sql = "INSERT INTO spark_doris SELECT \n" +
                "    province, \n" +
                "    COUNT(id) AS cnt, \n" +
                "    CURRENT_TIMESTAMP() AS create_time \n" +
                "FROM \n" +
                "    my_user\n" +
                "GROUP BY \n" +
                "    province;\n";
        spark.sql(sql);

        // 停止SparkSession，释放资源
        spark.stop();
    }
}
