package local.kongyu.myFlink.sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * 创建MySQL表并生成数据
 *
 * @author 孔余
 * @since 2024-03-06 17:19
 */
public class SQLMySQL {
    public static void main(String[] args) throws Exception {
        // 获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 创建流式表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 创建名为 my_user 的表
        tableEnv.executeSql("CREATE TABLE my_user_mysql(\n" +
                "  id BIGINT,\n" +
                "  name STRING,\n" +
                "  age INT,\n" +
                "  score DOUBLE,\n" +
                "  birthday TIMESTAMP(3),\n" +
                "  province STRING,\n" +
                "  city STRING,\n" +
                "  create_time TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "    'connector'='jdbc',\n" +
                "    'url' = 'jdbc:mysql://192.168.1.10:35725/kongyu_flink',\n" +
                "    'username' = 'root',\n" +
                "    'password' = 'Admin@123',\n" +
                "    'connection.max-retry-timeout' = '60s',\n" +
                "    'table-name' = 'my_user_mysql',\n" +
                "    'sink.buffer-flush.max-rows' = '500',\n" +
                "    'sink.buffer-flush.interval' = '5s',\n" +
                "    'sink.max-retries' = '3',\n" +
                "    'sink.parallelism' = '1'\n" +
                ");");

        // 执行 SQL 查询获取表数据
        Table table = tableEnv.sqlQuery("select * from my_user_mysql");
        // 执行表查询并打印结果
        TableResult result = table.execute();
        result.print();
    }
}
