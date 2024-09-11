# Java8 和 Flink1.18.1  SpringBoot2

**Flink On Spring**: 通过**maven-shade-plugin**插件将SpringBoot和Flink相关的依赖包打包在一起，然后通过集群运行。

## Standalone Session模式

**运行应用**

```
flink run MyFlinkOnSpringCluster-1.0.jar \
    --class=local.kongyu.flink.task.sql.DatagenToKafka \
    --method=run
```

## YARN Application 模式

**运行应用**

```
flink run-application -t yarn-application \
    -Dparallelism.default=3 \
    -Dtaskmanager.numberOfTaskSlots=3 \
    -Djobmanager.memory.process.size=2GB \
    -Dtaskmanager.memory.process.size=4GB \
    -Dyarn.application.name="生成数据并计算窗口" \
    MyFlinkOnSpringCluster-1.0.jar \
    --class=local.kongyu.flink.task.sql.DatagenToKafka \
    --method=run
```

## Kubernetes Operator 模式

参考[文档](https://github.com/kongyu666/work/blob/main/work/bigdata/05-flink/kubernetes-operator/deploy/flink-standard-myapp-prod.yaml)
