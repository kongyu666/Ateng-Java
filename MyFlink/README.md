# Flink 开发

https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/overview/

## 快速开始

将相关依赖包放在$FLINK_HOME/lib下

### 提交程序

```shell
flink run-application -t yarn-application \
    -Drest.port=0 \
    -Dparallelism.default=3 \
    -Dtaskmanager.numberOfTaskSlots=3 \
    -Djobmanager.memory.process.size=2GB \
    -Dtaskmanager.memory.process.size=4GB \
    -Dyarn.application.name="SQLKafkaWindow" \
    -c local.kongyu.myFlink.sql.window.SQLKafkaWindow \
    MyFlink-1.0.jar
```
