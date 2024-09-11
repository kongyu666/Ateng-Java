package local.kongyu.flink.task.sql;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.stereotype.Component;

/**
 * 生成数据并存入MinIO
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-07-21 08:07:31
 */
@Component
public class DatagenToMinIO {

    /**
     * 写入MinIO
     */
    public void run() {
        StreamExecutionEnvironment env = SpringUtil.getBean("flinkEnv", StreamExecutionEnvironment.class);
        StreamTableEnvironment tableEnv = SpringUtil.getBean("flinkTableEnv", StreamTableEnvironment.class);
        tableEnv.getConfig().set("pipeline.name", "生成数据写入到minio");
        env.enableCheckpointing(10000);
        // 创建表并设置水位线
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
                "  'rows-per-second' = '100',\n" +
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
        // 写入MinIO
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
                "    'path' = 's3a://test/flink/my_user',\n" +
                "    'format' = 'parquet'\n" +
                ");");
        tableEnv.executeSql("insert into my_user_file_minio select * from my_user;");
    }

}
