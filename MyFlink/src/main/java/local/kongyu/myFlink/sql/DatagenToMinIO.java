package local.kongyu.myFlink.sql;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.plugin.PluginManager;
import org.apache.flink.core.plugin.PluginUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * 创建表并生成数据
 *
 * @author 孔余
 * @since 2024-03-06 17:19
 */
public class DatagenToMinIO {
    public static void main(String[] args) throws Exception {
        // 初始化 s3 插件
        Configuration pluginConfiguration = new Configuration();
        pluginConfiguration.setString("s3.endpoint", "http://192.168.1.12:9000");
        pluginConfiguration.setString("s3.access-key", "admin");
        pluginConfiguration.setString("s3.secret-key", "Lingo@local_minio_9000");
        pluginConfiguration.setString("s3.path.style.access", "true");
        PluginManager pluginManager = PluginUtils.createPluginManagerFromRootFolder(pluginConfiguration);
        FileSystem.initialize(pluginConfiguration, pluginManager);

        // 获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(10000); // 每10秒执行一次检查点
        env.setParallelism(1);
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
                "  create_time TIMESTAMP_LTZ(3)\n" +
                ") WITH (\n" +
                "  'connector' = 'datagen',\n" +
                "  'rows-per-second' = '1',\n" +
                "  'fields.id.min' = '1',\n" +
                "  'fields.id.max' = '100000',\n" +
                "  'fields.name.length' = '10',\n" +
                "  'fields.age.min' = '18',\n" +
                "  'fields.age.max' = '60',\n" +
                "  'fields.score.min' = '0',\n" +
                "  'fields.score.max' = '100',\n" +
                "  'fields.province.length' = '5',\n" +
                "  'fields.city.length' = '5'\n" +
                ");");

        tableEnv.executeSql("CREATE TABLE my_user_file_minio (\n" +
                "  id BIGINT NOT NULL,\n" +
                "  name STRING,\n" +
                "  age INT,\n" +
                "  score DOUBLE,\n" +
                "  birthday TIMESTAMP(3),\n" +
                "  province STRING,\n" +
                "  city STRING,\n" +
                "  create_time TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "    'connector' = 'filesystem',\n" +
                "    'path' = 's3://test/flink',\n" +
                "    'format' = 'parquet'\n" +
                ");");
        // 插入一条测试数据
        tableEnv.executeSql(
                "INSERT INTO my_user_file_minio VALUES (\n" +
                        "  1, 'John Doe', 30, 95.5, TIMESTAMP '1993-06-15 08:30:00', 'SomeProvince', 'SomeCity', TIMESTAMP '2023-07-20 10:00:00'\n" +
                        ");"
        ).await();
        tableEnv.executeSql("insert into my_user_file_minio select * from my_user;");
        tableEnv.sqlQuery("select * from my_user_file_minio").execute().print();

    }
}
