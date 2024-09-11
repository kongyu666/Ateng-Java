package local.kongyu.spark.task.sql;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

/**
 * SparkSQL相关任务
 *
 * @author 孔余
 * @since 2024-02-28 11:35
 */
@Component
@Slf4j
public class MySparkSqlTask {

    /**
     * 执行SQL
     *
     */
    public void run() {
        // 获取环境
        SparkSession spark = SpringUtil.getBean("sparkSession", SparkSession.class);

        // 执行SQL查询
        Dataset<Row> ds = spark.sql("select * from my_user");

        // 显示 DataFrame 的结构
        ds.printSchema();

        // 显示查询结果
        ds.show();
        System.out.println("sparkSql任务运行成功");
    }
}
