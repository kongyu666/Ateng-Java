package local.kongyu.myFlink.sql;

import local.kongyu.myFlink.entity.UserEntity;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * 将表数据转换为数据流
 *
 * @author 孔余
 * @since 2024-03-06 17:19
 */
public class SQLToStream {
    public static void main(String[] args) throws Exception {
        // 获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 创建流式表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

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
        // 将表数据转换为数据流
        DataStream<UserEntity> dataStream = tableEnv.toDataStream(table, UserEntity.class);
        // 打印数据流
        dataStream.print("sql");

        // 执行流处理作业
        env.execute();
    }
}
