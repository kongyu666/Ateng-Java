# Flink开发文档

Flink 是一个开源的分布式流处理框架，专注于大规模数据流的实时处理。它提供了高吞吐量、低延迟的处理能力，支持有状态和无状态的数据流操作。Flink 可以处理事件时间、窗口化、流与批处理混合等复杂场景，广泛应用于实时数据分析、实时监控、机器学习等领域。其强大的容错机制和高可扩展性，使其成为大数据领域中的重要技术之一。

- [官网链接](https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/dev/datastream/overview/)



## 基础配置

### 创建项目

创建Maven项目，IDEA配置该项目SDK为JDK8、Maven的JRE也配置文JDK8、应用程序配置需要设置 `provided` 作用域

![image-20250114171619635](./assets/image-20250114171619635.png)



### 配置pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!-- 项目模型版本 -->
    <modelVersion>4.0.0</modelVersion>

    <!-- 项目坐标 -->
    <groupId>local.ateng.java</groupId>
    <artifactId>flink-examples</artifactId>
    <version>v1.0</version>
    <name>flink-examples</name>
    <description>
        Maven项目使用Java8对的Flink使用
    </description>

    <!-- 项目属性 -->
    <properties>
        <!-- 默认主程序 -->
        <start-class>local.ateng.java.WindowWordCount</start-class>
        <java.version>8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven-compiler.version>3.12.1</maven-compiler.version>
        <maven-shade.version>3.5.1</maven-shade.version>
        <lombok.version>1.18.36</lombok.version>
        <fastjson2.version>2.0.53</fastjson2.version>
        <hutool.version>5.8.35</hutool.version>
        <hadoop.version>3.3.6</hadoop.version>
        <flink.version>1.19.1</flink.version>
    </properties>

    <!-- 项目依赖 -->
    <dependencies>

        <!-- Hutool: Java工具库，提供了许多实用的工具方法 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>

        <!-- Lombok: 简化Java代码编写的依赖项 -->
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- 高性能的JSON库 -->
        <!-- https://github.com/alibaba/fastjson2/wiki/fastjson2_intro_cn#0-fastjson-20%E4%BB%8B%E7%BB%8D -->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
            <version>${fastjson2.version}</version>
        </dependency>

        <!-- JavaFaker: 用于生成虚假数据的Java库 -->
        <dependency>
            <groupId>com.github.javafaker</groupId>
            <artifactId>javafaker</artifactId>
            <version>1.0.2</version>
        </dependency>

        <!-- SLF4J API -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.36</version>
        </dependency>
        <!-- Log4j 2.x API -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.19.0</version>
        </dependency>
        <!-- Log4j 2.x 实现 -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.19.0</version>
        </dependency>
        <!-- SLF4J 和 Log4j 2.x 绑定 -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.19.0</version>
        </dependency>

        <!-- Apache Flink 客户端库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-clients</artifactId>
            <version>${flink.version}</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>

    <!-- 插件仓库配置 -->
    <repositories>
        <!-- Central Repository -->
        <repository>
            <id>central</id>
            <name>阿里云中央仓库</name>
            <url>https://maven.aliyun.com/repository/central</url>
            <!--<name>Maven官方中央仓库</name>
            <url>https://repo.maven.apache.org/maven2/</url>-->
        </repository>
    </repositories>

    <!-- 构建配置 -->
    <build>
        <finalName>${project.name}-${project.version}</finalName>
        <plugins>
            <!-- Maven 编译插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler.version}</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
            <!-- Maven Shade 打包插件 -->
            <!-- https://maven.apache.org/plugins/maven-shade-plugin/shade-mojo.html -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>${maven-shade.version}</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <!-- 禁用生成 dependency-reduced-pom.xml 文件 -->
                            <createDependencyReducedPom>false</createDependencyReducedPom>
                            <!-- 附加shaded工件时使用的分类器的名称 -->
                            <shadedClassifierName>shaded</shadedClassifierName>
                            <transformers>
                                <transformer
                                        implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <!-- 指定默认主程序 -->
                                    <mainClass>${start-class}</mainClass>
                                </transformer>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                            </transformers>
                            <artifactSet>
                                <!-- 排除依赖项 -->
                                <excludes>
                                    <exclude>org.apache.logging.log4j:*</exclude>
                                    <exclude>org.slf4j:*</exclude>
                                    <exclude>com.google.code.findbugs:jsr305</exclude>
                                </excludes>
                            </artifactSet>
                            <filters>
                                <!-- 不复制 META-INF 下的签名文件 -->
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>module-info.class</exclude>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.MF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                        <exclude>META-INF/*.txt</exclude>
                                        <exclude>META-INF/NOTICE</exclude>
                                        <exclude>META-INF/LICENSE</exclude>
                                        <exclude>META-INF/services/java.sql.Driver</exclude>
                                        <!-- 排除resources下的xml配置文件 -->
                                        <exclude>*.xml</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### 配置log4j2.xml

在**resources**目录下创建log4j2的日志配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{ISO8601} [%t] %-5level: %msg%n%throwable"/>
        </Console>
    </Appenders>

    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
        <Logger name="org.apache.flink" level="WARN" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>
        <Logger name="org.apache.kafka" level="ERROR" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>
    </Loggers>
</Configuration>
```

### 使用WordCount

创建第一个Flink程序WordCount

socketTextStream的地址需要自行修改，在Linux服务器需要安装安装nc（`sudo yum -y install nc`），然后使用 `nc -lk 9999` 启动Socket。

启动程序后，在nc上输入字符就开始统计数据了

```java
package local.ateng.java;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import java.time.Duration;

import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.util.Collector;

public class WindowWordCount {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple2<String, Integer>> dataStream = env
                .socketTextStream("192.168.1.12", 9999)
                .flatMap(new Splitter())
                .keyBy(value -> value.f0)
                .window(TumblingProcessingTimeWindows.of(Duration.ofSeconds(5)))
                .sum(1);

        dataStream.print();

        env.execute("Window WordCount");
    }

    public static class Splitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String sentence, Collector<Tuple2<String, Integer>> out) throws Exception {
            for (String word: sentence.split(" ")) {
                out.collect(new Tuple2<String, Integer>(word, 1));
            }
        }
    }

}
```

在nc终端输入以下内容：

```
java python
python java
ateng java
java kongyu
```

Flink程序就输入以下内容：

![image-20250114172836279](./assets/image-20250114172836279.png)



## DataStream

Flink 中的 DataStream 程序是对数据流（例如过滤、更新状态、定义窗口、聚合）进行转换的常规程序。数据流的起始是从各种源（例如消息队列、套接字流、文件）创建的。结果通过 sink 返回，例如可以将数据写入文件或标准输出（例如命令行终端）。Flink 程序可以在各种上下文中运行，可以独立运行，也可以嵌入到其它程序中。任务执行可以运行在本地 JVM 中，也可以运行在多台机器的集群上。

DataStream API 得名于特殊的 DataStream 类，该类用于表示 Flink 程序中的数据集合。你可以认为 它们是可以包含重复项的不可变数据集合。这些数据可以是有界（有限）的，也可以是无界（无限）的，但用于处理它们的API是相同的。

DataStream 在用法上类似于常规的 Java 集合，但在某些关键方面却大不相同。它们是不可变的，这意味着一旦它们被创建，你就不能添加或删除元素。你也不能简单地察看内部元素，而只能使用 DataStream API 操作来处理它们，DataStream API 操作也叫作转换（transformation）。

你可以通过在 Flink 程序中添加 source 创建一个初始的 DataStream。然后，你可以基于 DataStream 派生新的流，并使用 map、filter 等 API 方法把 DataStream 和派生的流连接在一起。

- [官网地址](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/dev/datastream/overview/)



### 数据生成

#### 创建实体类

```java
package local.ateng.java.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户信息实体类
 * 用于表示系统中的用户信息。
 *
 * @author 孔余
 * @since 2024-01-10 15:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户年龄
     * 注意：这里使用Integer类型，表示年龄是一个整数值。
     */
    private Integer age;

    /**
     * 分数
     */
    private Double score;

    /**
     * 用户生日
     * 注意：这里使用Date类型，表示用户的生日。
     */
    private Date birthday;

    /**
     * 用户所在省份
     */
    private String province;

    /**
     * 用户所在城市
     */
    private String city;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
```

#### 创建生成器函数

```java
package local.ateng.java.function;

import com.github.javafaker.Faker;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.connector.datagen.source.GeneratorFunction;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * 生成器函数
 *
 * @author 孔余
 * @since 2024-02-29 17:07
 */
public class MyGeneratorFunction implements GeneratorFunction {
    // 创建一个Java Faker实例，指定Locale为中文
    private Faker faker;

    // 初始化随机数数据生成器
    @Override
    public void open(SourceReaderContext readerContext) throws Exception {
        faker = new Faker(new Locale("zh-CN"));
    }

    @Override
    public UserInfoEntity map(Object value) throws Exception {
        // 使用 随机数数据生成器 来创建实例
        UserInfoEntity user = UserInfoEntity.builder()
                .id(System.currentTimeMillis())
                .name(faker.name().fullName())
                .birthday(faker.date().birthday())
                .age(faker.number().numberBetween(0, 100))
                .province(faker.address().state())
                .city(faker.address().cityName())
                .score(faker.number().randomDouble(3, 1, 100))
                .createTime(LocalDateTime.now())
                .build();
        return user;
    }

}
```

#### 创建数据生成器

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/datagen/)

```java
package local.ateng.java.DataStream.sink;

import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 数据生成连接器，用于生成模拟数据并将其输出到 Flink 流中。
 *
 * @author 孔余
 * @since 2024-02-29 16:55
 */
public class DataGenerator {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);

        // 创建数据生成器源，生成器函数为 MyGeneratorFunction，生成 Long.MAX_VALUE 条数据，速率限制为 3 条/秒
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(),
                Long.MAX_VALUE,
                RateLimiterStrategy.perSecond(3),
                TypeInformation.of(UserInfoEntity.class)
        );

        // 将数据生成器源添加到流中
        DataStreamSource<UserInfoEntity> stream =
                env.fromSource(source,
                        WatermarkStrategy.noWatermarks(), // 不生成水印，仅用于演示
                        "Generator Source");

        // 打印流中的数据
        stream.print();

        // 执行 Flink 作业
        env.execute();
    }
}
```

![image-20250114175146261](./assets/image-20250114175146261.png)



#### 创建数据生成器（Kafka）

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/kafka/)

**添加依赖**

```xml
    <properties>
        <flink-kafka.version>3.3.0-1.19</flink-kafka.version>
    </properties>

    <dependencies>
        <!-- Apache Flink 连接器基础库库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-base</artifactId>
            <version>${flink.version}</version>
            <scope>provided</scope>
        </dependency>
        <!-- Apache Flink Kafka 连接器库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-kafka</artifactId>
            <version>${flink-kafka.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

**创建生成器**

```java
package local.ateng.java.DataStream.sink;

import com.alibaba.fastjson2.JSONObject;
import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 示例说明：
 * 此类演示了如何使用 Flink 的 DataGenConnector 和 KafkaSink 将生成的模拟数据发送到 Kafka 中。
 * 1. 创建 DataGeneratorSource 生成模拟数据；
 * 2. 使用 map 函数将 UserInfoEntity 转换为 JSON 字符串；
 * 3. 配置 KafkaSink 将数据发送到 Kafka 中。
 *
 * @author 孔余
 * @since 2024-02-29 16:55
 */
public class DataGeneratorKafka {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // Kafka的Topic有多少个分区就设置多少并行度（可以设置为分区的倍数），例如：Topic有3个分区就设置并行度为3
        env.setParallelism(3);

        // 创建 DataGeneratorSource 生成模拟数据
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(), // 自定义的生成器函数
                Long.MAX_VALUE, // 生成数据的数量
                RateLimiterStrategy.perSecond(10), // 生成数据的速率限制
                TypeInformation.of(UserInfoEntity.class) // 数据类型信息
        );

        // 将生成的 UserInfoEntity 对象转换为 JSON 字符串
        SingleOutputStreamOperator<String> stream = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "Generator Source")
                .map(user -> JSONObject.toJSONString(user));

        // 配置 KafkaSink 将数据发送到 Kafka 中
        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers("192.168.1.10:9094") // Kafka 服务器地址和端口
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("ateng_flink_json") // Kafka 主题
                        .setValueSerializationSchema(new SimpleStringSchema()) // 数据序列化方式
                        .build()
                )
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE) // 传输保障级别
                .build();

        // 将数据打印到控制台
        stream.print("sink kafka");
        // 将数据发送到 Kafka
        stream.sinkTo(sink);

        // 执行程序
        env.execute();
    }
}
```

![image-20250114193523483](./assets/image-20250114193523483.png)

![image-20250114193549354](./assets/image-20250114193549354.png)



#### 创建数据生成器（Doris）

参考：[官方文档](https://doris.apache.org/zh-CN/docs/ecosystem/flink-doris-connector)

**添加依赖**

```xml
    <dependencies>
        <!-- Apache Flink Doris 连接器库 -->
        <dependency>
            <groupId>org.apache.doris</groupId>
            <artifactId>flink-doris-connector-1.19</artifactId>
            <version>24.1.0</version>
        </dependency>
    </dependencies>
```

**创建生成器**

注意重复执行需要修改 **LabelPrefix** ，详情参考[官方文档](https://doris.apache.org/zh-CN/docs/ecosystem/flink-doris-connector#sink-%E9%85%8D%E7%BD%AE%E9%A1%B9)

```sql
package local.ateng.java.DataStream.sink;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import local.ateng.java.entity.UserInfoEntity;
import local.ateng.java.function.MyGeneratorFunction;
import org.apache.doris.flink.cfg.DorisExecutionOptions;
import org.apache.doris.flink.cfg.DorisOptions;
import org.apache.doris.flink.cfg.DorisReadOptions;
import org.apache.doris.flink.sink.DorisSink;
import org.apache.doris.flink.sink.writer.serializer.SimpleStringSerializer;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDateTime;
import java.util.Properties;


/**
 * 数据生成连接器，用于生成模拟数据并将其输出到Doris中
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-15
 */
public class DataGeneratorDoris {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);
        // 启用检查点，设置检查点间隔为 10 秒，检查点模式为 精准一次
        env.enableCheckpointing(10 * 1000, CheckpointingMode.EXACTLY_ONCE);

        // 创建数据生成器源，生成器函数为 MyGeneratorFunction，每秒生成 1000 条数据，速率限制为 3 条/秒
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(),
                1000,
                RateLimiterStrategy.perSecond(3),
                TypeInformation.of(UserInfoEntity.class)
        );

        // 将数据生成器源添加到流中
        SingleOutputStreamOperator<String> stream = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "Generator Source")
                .map(user -> {
                    // 转换成和 Doris 表字段一致
                    LocalDateTime createTime = user.getCreateTime();
                    user.setCreateTime(null);
                    JSONObject jsonObject = BeanUtil.toBean(user, JSONObject.class);
                    jsonObject.put("create_time", createTime);
                    return JSONObject.toJSONString(jsonObject);
                });

        // Sink
        DorisSink.Builder<String> builder = DorisSink.builder();
        DorisOptions.Builder dorisBuilder = DorisOptions.builder();
        // doris 相关信息
        dorisBuilder.setFenodes("192.168.1.12:9040")
                .setTableIdentifier("kongyu_flink.my_user") // db.table
                .setUsername("admin")
                .setPassword("Admin@123");
        Properties properties = new Properties();
        // JSON 格式需要设置的参数
        properties.setProperty("format", "json");
        properties.setProperty("read_json_by_line", "true");
        DorisExecutionOptions.Builder executionBuilder = DorisExecutionOptions.builder();
        // Stream load 导入使用的 label 前缀。2pc 场景下要求全局唯一，用来保证 Flink 的 EOS 语义。
        executionBuilder.setLabelPrefix("label-doris2")
                .setDeletable(false)
                .setStreamLoadProp(properties);
        builder.setDorisReadOptions(DorisReadOptions.builder().build())
                .setDorisExecutionOptions(executionBuilder.build())
                .setSerializer(new SimpleStringSerializer())
                .setDorisOptions(dorisBuilder.build());
        DorisSink<String> sink = builder.build();

        // 将数据打印到控制台
        stream.print("sink doris");
        // 打印流中的数据
        stream.sinkTo(sink);

        // 执行 Flink 作业
        env.execute();
    }
}
```

**创建表**

创建表

```sql
DROP TABLE IF EXISTS my_user;
CREATE TABLE IF NOT EXISTS my_user (
    id BIGINT NOT NULL,
    create_time DATETIME,
    name STRING,
    age INT,
    score DOUBLE,
    birthday DATETIME,
    province STRING,
    city STRING
)
DUPLICATE KEY (id, create_time)
DISTRIBUTED BY HASH(id) BUCKETS 10
PROPERTIES (
    "replication_allocation" = "tag.location.default: 1"
);
```

查看表

```sql
SHOW CREATE TABLE my_user\G;
```

查看数据

```sql
select * from my_user;
```



#### 创建数据生成器（MySQL）

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/jdbc/)

**添加依赖**

```xml
    <properties>
        <flink-jdbc.version>3.2.0-1.19</flink-jdbc.version>
    </properties>

    <dependencies>
		<!-- Apache Flink JDBC 连接器库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-jdbc</artifactId>
            <version>${flink-jdbc.version}</version>
            <scope>provided</scope>
        </dependency>
        <!-- MySQL驱动 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.33</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

**创建生成器**

```java
package local.ateng.java.DataStream.sink;

import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Timestamp;

/**
 * 数据生成连接器，用于生成模拟数据并将其输出到Mysql中。
 *
 * @author 孔余
 * @since 2024-03-06 16:11
 */
public class DataGeneratorMySQL {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);

        // 创建数据生成器源，生成器函数为 MyGeneratorFunction，每秒生成 1000 条数据，速率限制为 3 条/秒
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(),
                1000,
                RateLimiterStrategy.perSecond(3),
                TypeInformation.of(UserInfoEntity.class)
        );

        // 将数据生成器源添加到流中
        DataStreamSource<UserInfoEntity> stream =
                env.fromSource(source,
                        WatermarkStrategy.noWatermarks(), // 不生成水印，仅用于演示
                        "Generator Source");

        /**
         * 写入mysql
         * JDBCSink的4个参数:
         *    第一个参数： 执行的sql，一般就是 insert into
         *    第二个参数： 预编译sql， 对占位符填充值
         *    第三个参数： 执行选项 ---》 攒批、重试
         *    第四个参数： 连接选项 ---》 url、用户名、密码
         */
        SinkFunction<UserInfoEntity> jdbcSink = JdbcSink.sink(
                "insert into my_user(name,age,score,birthday,province,city) values(?,?,?,?,?,?)",
                (preparedStatement, user) -> {
                    //每收到一条UserInfoEntity，如何去填充占位符
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setInt(2, user.getAge());
                    preparedStatement.setDouble(3, user.getScore());
                    preparedStatement.setTimestamp(4, new Timestamp(user.getBirthday().getTime()));
                    preparedStatement.setString(5, user.getProvince());
                    preparedStatement.setString(6, user.getCity());
                },
                JdbcExecutionOptions.builder()
                        .withMaxRetries(3) // 重试次数
                        .withBatchSize(1000) // 批次的大小：条数
                        .withBatchIntervalMs(3000) // 批次的时间
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://192.168.1.10:35725/kongyu_flink")
                        .withUsername("root")
                        .withPassword("Admin@123")
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .withConnectionCheckTimeoutSeconds(60) // 重试的超时时间
                        .build()
        );

        // 将数据打印到控制台
        stream.print("sink mysql");
        // 打印流中的数据
        stream.addSink(jdbcSink);

        // 执行 Flink 作业
        env.execute();
    }
}
```

**创建表**

```sql
DROP TABLE IF EXISTS my_user;
CREATE TABLE IF NOT EXISTS my_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,          -- 用户唯一标识，主键自动递增
    name        VARCHAR(50) DEFAULT NULL,                   -- 增加name字段长度，并设置默认值为NULL
    age         INT DEFAULT NULL,                           -- 年龄，默认为NULL
    score       DOUBLE DEFAULT 0.0,                         -- 成绩，默认为0.0
    birthday    DATE DEFAULT NULL,                          -- 生日，默认为NULL
    province    VARCHAR(50) DEFAULT NULL,                   -- 省份，增加字段长度
    city        VARCHAR(50) DEFAULT NULL,                   -- 城市，增加字段长度
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),  -- 创建时间，默认当前时间
    INDEX idx_name (name),                                  -- 如果name经常用于查询，可以考虑为其加索引
    INDEX idx_province_city (province, city)                -- 如果经常根据省市进行查询，创建联合索引
);
```

![image-20250114195605661](./assets/image-20250114195605661.png)

![image-20250114195632746](./assets/image-20250114195632746.png)



#### 创建数据生成器（PostgreSQL）

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/jdbc/)

**添加依赖**

```xml
    <properties>
        <flink-jdbc.version>3.2.0-1.19</flink-jdbc.version>
    </properties>

    <dependencies>
		<!-- Apache Flink JDBC 连接器库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-jdbc</artifactId>
            <version>${flink-jdbc.version}</version>
            <scope>provided</scope>
        </dependency>
        <!-- PostgreSQL驱动 -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.1</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

**创建生成器**

```java
package local.ateng.java.DataStream.sink;

import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Timestamp;

/**
 * 数据生成连接器，用于生成模拟数据并将其输出到PostgreSQL中
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-14
 */
public class DataGeneratorPostgreSQL {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);

        // 创建数据生成器源，生成器函数为 MyGeneratorFunction，每秒生成 1000 条数据，速率限制为 3 条/秒
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(),
                1000,
                RateLimiterStrategy.perSecond(3),
                TypeInformation.of(UserInfoEntity.class)
        );

        // 将数据生成器源添加到流中
        DataStreamSource<UserInfoEntity> stream =
                env.fromSource(source,
                        WatermarkStrategy.noWatermarks(), // 不生成水印，仅用于演示
                        "Generator Source");

        /**
         * 写入mysql
         * JDBCSink的4个参数:
         *    第一个参数： 执行的sql，一般就是 insert into
         *    第二个参数： 预编译sql， 对占位符填充值
         *    第三个参数： 执行选项 ---》 攒批、重试
         *    第四个参数： 连接选项 ---》 url、用户名、密码
         */
        SinkFunction<UserInfoEntity> jdbcSink = JdbcSink.sink(
                "insert into my_user(name,age,score,birthday,province,city) values(?,?,?,?,?,?)",
                (preparedStatement, user) -> {
                    //每收到一条UserInfoEntity，如何去填充占位符
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setInt(2, user.getAge());
                    preparedStatement.setDouble(3, user.getScore());
                    preparedStatement.setTimestamp(4, new Timestamp(user.getBirthday().getTime()));
                    preparedStatement.setString(5, user.getProvince());
                    preparedStatement.setString(6, user.getCity());
                },
                JdbcExecutionOptions.builder()
                        .withMaxRetries(3) // 重试次数
                        .withBatchSize(1000) // 批次的大小：条数
                        .withBatchIntervalMs(3000) // 批次的时间
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:postgresql://192.168.1.10:32297/kongyu_flink?currentSchema=public")
                        .withUsername("postgres")
                        .withPassword("Lingo@local_postgresql_5432")
                        .withDriverName("org.postgresql.Driver")
                        .withConnectionCheckTimeoutSeconds(60) // 重试的超时时间
                        .build()
        );

        // 将数据打印到控制台
        stream.print("sink postgres");
        // 打印流中的数据
        stream.addSink(jdbcSink);

        // 执行 Flink 作业
        env.execute();
    }
}
```

**创建表**

```sql
DROP TABLE IF EXISTS my_user;
CREATE TABLE IF NOT EXISTS my_user (
    id          BIGSERIAL PRIMARY KEY,                         -- BIGSERIAL 自动递增主键
    name        VARCHAR(50) DEFAULT NULL,                      -- 增加 name 字段长度，默认 NULL
    age         INTEGER DEFAULT NULL,                          -- 使用 INTEGER 替代 INT
    score       DOUBLE PRECISION DEFAULT 0.0,                  -- 使用 DOUBLE PRECISION
    birthday    DATE DEFAULT NULL,                             -- 生日字段，默认 NULL
    province    VARCHAR(50) DEFAULT NULL,                      -- 省份字段，增加长度
    city        VARCHAR(50) DEFAULT NULL,                      -- 城市字段，增加长度
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3)      -- 创建时间字段，默认当前时间
);
-- 创建普通索引
CREATE INDEX idx_name ON my_user(name);                       -- 为 name 字段创建普通索引
CREATE INDEX idx_province_city ON my_user(province, city);     -- 为 province 和 city 字段创建联合索引
```

![image-20250114205417333](./assets/image-20250114205417333.png)

![image-20250114205434028](./assets/image-20250114205434028.png)



#### 创建数据生成器（MongoDB）

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/mongodb/)

**添加依赖**

```xml
    <properties>
        <flink-mongodb.version>1.2.0-1.19</flink-mongodb.version>
    </properties>

    <dependencies>
		<!-- Apache Flink MongoDB 连接器库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-mongodb</artifactId>
            <version>${flink-mongodb.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

**创建生成器**

```java
package local.ateng.java.DataStream.sink;

import com.alibaba.fastjson2.JSONObject;
import com.mongodb.client.model.InsertOneModel;
import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.mongodb.sink.MongoSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.bson.BsonDocument;

/**
 * 数据生成连接器，用于生成模拟数据并将其输出到MongoDB中
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-14
 */
public class DataGeneratorMongoDB {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);

        // 创建数据生成器源，生成器函数为 MyGeneratorFunction，每秒生成 1000 条数据，速率限制为 3 条/秒
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(),
                1000,
                RateLimiterStrategy.perSecond(3),
                TypeInformation.of(UserInfoEntity.class)
        );

        // 将数据生成器源添加到流中
        DataStreamSource<UserInfoEntity> stream =
                env.fromSource(source,
                        WatermarkStrategy.noWatermarks(), // 不生成水印，仅用于演示
                        "Generator Source");

        // MongoDB Sink
        MongoSink<UserInfoEntity> sink = MongoSink.<UserInfoEntity>builder()
                .setUri("mongodb://root:Admin%40123@192.168.1.10:33627")
                .setDatabase("kongyu_flink")
                .setCollection("my_user")
                .setBatchSize(1000)
                .setBatchIntervalMs(3000)
                .setMaxRetries(3)
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .setSerializationSchema(
                        (input, context) -> new InsertOneModel<>(BsonDocument.parse(JSONObject.toJSONString(input))))
                .build();

        // 将数据打印到控制台
        stream.print("sink mongodb");
        // 写入数据
        stream.sinkTo(sink);

        // 执行 Flink 作业
        env.execute();
    }
}
```

![image-20250114210540277](./assets/image-20250114210540277.png)

![image-20250114210603464](./assets/image-20250114210603464.png)

#### 创建数据生成器（OpenSearch）

注意以下事项：

- 支持 **OpenSearch1.x** ，1.3.19 版本验证通过
- 支持 **ElasticSearch7.x** ，7.17.26 版本验证通过

- 不支持 OpenSearch2.x
- 不支持 ElasticSearch8.x

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/mongodb/)

**添加依赖**

```xml
    <properties>
        <flink-opensearch.version>1.2.0-1.19</flink-opensearch.version>
    </properties>

    <dependencies>
		<!-- Apache Flink OpenSearch 连接器库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-opensearch</artifactId>
            <version>${flink-opensearch.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

**创建生成器**

```java
package local.ateng.java.DataStream.sink;

import cn.hutool.core.bean.BeanUtil;
import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.opensearch.sink.FlushBackoffType;
import org.apache.flink.connector.opensearch.sink.OpensearchSink;
import org.apache.flink.connector.opensearch.sink.OpensearchSinkBuilder;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.http.HttpHost;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.Requests;

import java.util.Map;

/**
 * 数据生成连接器，用于生成模拟数据并将其输出到OpenSearch中
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-14
 */
public class DataGeneratorOpenSearch {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);

        // 创建数据生成器源，生成器函数为 MyGeneratorFunction，每秒生成 1000 条数据，速率限制为 3 条/秒
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(),
                Long.MAX_VALUE,
                RateLimiterStrategy.perSecond(10),
                TypeInformation.of(UserInfoEntity.class)
        );

        // 将数据生成器源添加到流中
        DataStreamSource<UserInfoEntity> stream =
                env.fromSource(source,
                        WatermarkStrategy.noWatermarks(), // 不生成水印，仅用于演示
                        "Generator Source");

        // Sink
        OpensearchSink<UserInfoEntity> sink = new OpensearchSinkBuilder<UserInfoEntity>()
                .setHosts(new HttpHost("192.168.1.10", 31723, "http"))
                .setEmitter(
                        (element, context, indexer) ->
                                indexer.add(createIndexRequest(element)))
                .setBulkFlushMaxActions(1000)
                .setBulkFlushInterval(3000)
                .setBulkFlushMaxSizeMb(10)
                .setBulkFlushBackoffStrategy(FlushBackoffType.EXPONENTIAL, 5, 1000)
                .build();

        // 将数据打印到控制台
        stream.print("sink opensearch");
        // 写入数据
        stream.sinkTo(sink);

        // 执行 Flink 作业
        env.execute();
    }

    private static IndexRequest createIndexRequest(UserInfoEntity element) {
        Map map = BeanUtil.toBean(element, Map.class);
        return Requests.indexRequest()
                //.id(String.valueOf(element.getId()))
                .index("my_user")
                .source(map);
    }
}
```

**查看索引**

创建索引，可以选择手动创建索引，Flink自动创建的索引可能其设置不满足相关需求

```
curl -X PUT "http://localhost:9200/my_user" -H 'Content-Type: application/json' -d '{
  "settings": {
    "mapping.total_fields.limit": "1000000",
    "max_result_window": "1000000",
    "number_of_shards": 1,
    "number_of_replicas": 0
  }
}'
```

查看所有索引

```
curl http://localhost:9200/_cat/indices?v
```

查看索引的统计信息

```
curl "http://localhost:9200/my_user/_stats/docs?pretty"
```

查看索引映射（mappings）和设置

```
curl http://localhost:9200/my_user/_mapping?pretty
curl http://localhost:9200/my_user/_settings?pretty
```

查看数据

```
curl -X GET "http://localhost:9200/my_user/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "createTime": {
        "order": "desc"
      }
    }
  ],
  "size": 10
}'
```

删除索引

```
curl -X DELETE "http://localhost:9200/my_user"
```

#### 创建数据生成器（HDFS）

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/filesystem/)

**添加依赖**

```xml
    <dependencies>
        <!-- Hadoop HDFS客户端 -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-client</artifactId>
            <version>${hadoop.version}</version>
            <scope>provided</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-reload4j</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>ch.qos.logback</groupId>
                    <artifactId>logback-classic</artifactId>
                </exclusion>
                <exclusion>
                    <artifactId>gson</artifactId>
                    <groupId>com.google.code.gson</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- Apache Flink Files 连接器库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-files</artifactId>
            <version>${flink.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

**创建生成器**

```java
package local.ateng.java.DataStream.sink;

import com.alibaba.fastjson2.JSONObject;
import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.protobuf.ParquetProtoWriters;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;

/**
 * 数据生成连接器，用于生成模拟数据并将其输出到HDFS中
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-15
 */
public class DataGeneratorHDFS {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 启用检查点，设置检查点间隔为 2 分钟，检查点模式为 精准一次
        env.enableCheckpointing(120 * 1000, CheckpointingMode.EXACTLY_ONCE);
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);

        // 创建 DataGeneratorSource 生成模拟数据
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(), // 自定义的生成器函数
                Long.MAX_VALUE, // 生成数据的数量
                RateLimiterStrategy.perSecond(10), // 生成数据的速率限制
                TypeInformation.of(UserInfoEntity.class) // 数据类型信息
        );

        // 将生成的 UserInfoEntity 对象转换为 JSON 字符串
        SingleOutputStreamOperator<String> stream = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "Generator Source")
                .map(user -> JSONObject.toJSONString(user));

        // Sink
        FileSink<String> sink = FileSink
                .forRowFormat(new Path("hdfs://server01:8020/data/flink/sink"), new SimpleStringEncoder<String>("UTF-8"))
                // 基于 Checkpoint 的滚动策略
                .withRollingPolicy(OnCheckpointRollingPolicy.build())
                .build();

        // 将数据打印到控制台
        stream.print("sink hdfs");
        // 写入数据
        stream.sinkTo(sink);

        // 执行程序
        env.execute();
    }
}
```

**配置HDFS**

Windows操作系统配置Hadoop

- 参考：[地址](https://github.com/cdarlint/winutils/tree/master)

操作系统设置环境变量

```
HADOOP_GROUP_NAME=ateng
HADOOP_USER_NAME=admin
```

创建目录并设置权限

```
hadoop fs -mkdir -p /data
hadoop fs -chown admin:ateng /data
```

查看文件

```
hadoop fs -ls /data/flink/sink
```

![image-20250115093703501](./assets/image-20250115093703501.png)



#### 创建数据生成器（MinIO）

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/filesystem/)

**添加依赖**

```xml
    <dependencies>
        <!-- S3 连接器依赖 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-s3-fs-hadoop</artifactId>
            <version>${flink.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-aws</artifactId>
            <version>${hadoop.version}</version>
            <scope>provided</scope>
        </dependency>
        <!-- Apache Flink Files 连接器库 -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-files</artifactId>
            <version>${flink.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

**创建生成器**

```java
package local.ateng.java.DataStream.sink;

import com.alibaba.fastjson2.JSONObject;
import local.ateng.java.function.MyGeneratorFunction;
import local.ateng.java.entity.UserInfoEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.plugin.PluginManager;
import org.apache.flink.core.plugin.PluginUtils;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;

/**
 * 数据生成连接器，用于生成模拟数据并将其输出到MinIO中
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-15
 */
public class DataGeneratorMinIO {
    public static void main(String[] args) throws Exception {
        // 初始化 s3 插件
        Configuration pluginConfiguration = new Configuration();
        pluginConfiguration.setString("s3.endpoint", "http://192.168.1.13:9000");
        pluginConfiguration.setString("s3.access-key", "admin");
        pluginConfiguration.setString("s3.secret-key", "Lingo@local_minio_9000");
        pluginConfiguration.setString("s3.path.style.access", "true");
        PluginManager pluginManager = PluginUtils.createPluginManagerFromRootFolder(pluginConfiguration);
        FileSystem.initialize(pluginConfiguration, pluginManager);

        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 启用检查点，设置检查点间隔为 2 分钟，检查点模式为 精准一次
        env.enableCheckpointing(120 * 1000, CheckpointingMode.EXACTLY_ONCE);
        // 设置并行度为 1，仅用于简化示例
        env.setParallelism(1);

        // 创建 DataGeneratorSource 生成模拟数据
        DataGeneratorSource<UserInfoEntity> source = new DataGeneratorSource<>(
                new MyGeneratorFunction(), // 自定义的生成器函数
                Long.MAX_VALUE, // 生成数据的数量
                RateLimiterStrategy.perSecond(10), // 生成数据的速率限制
                TypeInformation.of(UserInfoEntity.class) // 数据类型信息
        );

        // 将生成的 UserInfoEntity 对象转换为 JSON 字符串
        SingleOutputStreamOperator<String> stream = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "Generator Source")
                .map(user -> JSONObject.toJSONString(user));

        // Sink
        FileSink<String> sink = FileSink
                .forRowFormat(new Path("s3a://test/flink/sink"), new SimpleStringEncoder<String>("UTF-8"))
                // 基于 Checkpoint 的滚动策略
                .withRollingPolicy(OnCheckpointRollingPolicy.build())
                .build();

        // 将数据打印到控制台
        stream.print("sink minio");
        // 写入数据
        stream.sinkTo(sink);

        // 执行程序
        env.execute();
    }
}
```

**查看文件**

```
mcli ls minio-server/test/flink/sink/2025-01-15--10/
```

![image-20250115102715787](./assets/image-20250115102715787.png)



### 数据源

#### Kafka

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/kafka/)

```java
package local.ateng.java.DataStream.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

/**
 * Apache Kafka 连接器
 * https://nightlies.apache.org/flink/flink-docs-master/zh/docs/connectors/datastream/kafka/
 * Kafka 数据流处理，用于从 Kafka 主题接收 JSON 数据，并对用户年龄进行统计。
 *
 * @author 孔余
 * @since 2024-02-29 15:59
 */
public class DataSourceKafka {

    public static void main(String[] args) throws Exception {

        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 启用检查点，设置检查点间隔为 5 秒，检查点模式为 精准一次
        env.enableCheckpointing(5 * 1000, CheckpointingMode.EXACTLY_ONCE);
        // Kafka的Topic有多少个分区就设置多少并行度（可以设置为分区的倍数），例如：Topic有3个分区就设置并行度为3
        env.setParallelism(3);

        // 创建 Kafka 数据源，连接到指定的 Kafka 服务器和主题
        KafkaSource<String> source = KafkaSource.<String>builder()
                // 设置 Kafka 服务器地址
                .setBootstrapServers("192.168.1.10:9094")
                // 设置要订阅的主题
                .setTopics("ateng_flink_json")
                // 设置消费者组 ID
                .setGroupId("ateng")
                // 设置在检查点时提交偏移量（offsets）以确保精确一次语义
                .setProperty("commit.offsets.on.checkpoint", "true")
                // 启用自动提交偏移量
                .setProperty("enable.auto.commit", "true")
                // 自动提交偏移量的时间间隔
                .setProperty("auto.commit.interval.ms", "1000")
                // 设置分区发现的时间间隔
                .setProperty("partition.discovery.interval.ms", "10000")
                // 设置起始偏移量（如果没有提交的偏移量，则从最早的偏移量开始消费）
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                // 设置仅接收值的反序列化器
                .setValueOnlyDeserializer(new SimpleStringSchema())
                // 构建 Kafka 数据源
                .build();

        // 从 source 中读取数据
        DataStreamSource<String> dataStream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        // 打印计算结果
        dataStream.print("output");

        // 执行流处理作业
        env.execute("Kafka Source");
    }

}
```

#### HDFS

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/filesystem/)

```java
package local.ateng.java.DataStream.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.StreamFormat;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;

/**
 * 读取HDFS文件
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-15
 */
public class DataSourceHDFS {

    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 启用检查点，设置检查点间隔为 120 秒，检查点模式为 精准一次
        env.enableCheckpointing(120 * 1000, CheckpointingMode.EXACTLY_ONCE);
        // 设置并行度为 1
        env.setParallelism(1);

        // 创建 FileSource 从 HDFS 中持续读取数据
        FileSource<String> fileSource = FileSource
                .forRecordStreamFormat(new TextLineInputFormat(), new Path("hdfs://server01:8020/data/flink/sink"))
                .monitorContinuously(Duration.ofMillis(5))
                .build();

        // 从 Source 中读取数据
        DataStream<String> stream = env.fromSource(fileSource, WatermarkStrategy.noWatermarks(), "HDFS Source");

        // 输出流数据
        stream.print("output");

        // 执行程序
        env.execute("HDFS Source");
    }

}
```

#### MinIO

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/connectors/datastream/filesystem/)

```java
package local.ateng.java.DataStream.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.plugin.PluginManager;
import org.apache.flink.core.plugin.PluginUtils;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;

/**
 * 读取MinIO文件
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-15
 */
public class DataSourceMinIO {

    public static void main(String[] args) throws Exception {
        // 初始化 s3 插件
        Configuration pluginConfiguration = new Configuration();
        pluginConfiguration.setString("s3.endpoint", "http://192.168.1.13:9000");
        pluginConfiguration.setString("s3.access-key", "admin");
        pluginConfiguration.setString("s3.secret-key", "Lingo@local_minio_9000");
        pluginConfiguration.setString("s3.path.style.access", "true");
        PluginManager pluginManager = PluginUtils.createPluginManagerFromRootFolder(pluginConfiguration);
        FileSystem.initialize(pluginConfiguration, pluginManager);

        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 启用检查点，设置检查点间隔为 120 秒，检查点模式为 精准一次
        env.enableCheckpointing(120 * 1000, CheckpointingMode.EXACTLY_ONCE);
        // 设置并行度为 1
        env.setParallelism(1);

        // 创建 FileSource 从 MinIO 中持续读取数据
        FileSource<String> fileSource = FileSource
                .forRecordStreamFormat(new TextLineInputFormat(), new Path("s3a://test/flink/sink"))
                .monitorContinuously(Duration.ofMillis(5))
                .build();

        // 从 Source 中读取数据
        DataStream<String> stream = env.fromSource(fileSource, WatermarkStrategy.noWatermarks(), "MinIO Source");

        // 输出流数据
        stream.print("output");

        // 执行程序
        env.execute("MinIO Source");
    }

}
```



### 算子

参考：[官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.19/zh/docs/dev/datastream/operators/overview/)
