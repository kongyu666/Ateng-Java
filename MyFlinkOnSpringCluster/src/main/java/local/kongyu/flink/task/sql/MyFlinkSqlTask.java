package local.kongyu.flink.task.sql;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.stereotype.Component;

/**
 * Flink SQL任务
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-07-19 11:22:41
 */
@Component
public class MyFlinkSqlTask {

    public void run() {
        // 获取环境
        StreamTableEnvironment tableEnv = SpringUtil.getBean("flinkTableEnv", StreamTableEnvironment.class);

        // 创建名为 my_user 的表，使用 DataGen connector 生成测试数据
        tableEnv.executeSql("CREATE TABLE my_user (\n" +
                "  id BIGINT NOT NULL,\n" +
                "  name STRING,\n" +
                "  age INT,\n" +
                "  score DOUBLE,\n" +
                "  birthday TIMESTAMP(3),\n" +
                "  province STRING,\n" +
                "  city STRING,\n" +
                "  create_time TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "  'connector' = 'datagen',\n" +
                "  'rows-per-second' = '1', \n" +
                "  'fields.id.kind' = 'sequence',\n" +
                "  'fields.id.start' = '1',\n" +
                "  'fields.id.end' = '1000000',\n" +
                "  'fields.name.length' = '10',\n" +
                "  'fields.age.min' = '18',\n" +
                "  'fields.age.max' = '60',\n" +
                "  'fields.score.min' = '0',\n" +
                "  'fields.score.max' = '100',\n" +
                "  'fields.province.length' = '5',\n" +
                "  'fields.city.length' = '5'\n" +
                ");\n");

        // 执行 SQL 查询获取表数据
        Table table = tableEnv.sqlQuery("select * from my_user");
        // 执行表查询并打印结果
        TableResult result = table.execute();
        result.print();
    }
}
