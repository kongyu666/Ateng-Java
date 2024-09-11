# Java8 和 Spark3.5  SpringBoot2.7

## Spark Cluster

```shell
spark-submit \
    --master spark://bigdata01:7077 \
    --deploy-mode cluster \
    --total-executor-cores 2 \
    --class local.kongyu.spark.SparkClusterApplication \
    MySparkOnSpringCluster-1.0.jar \
    --class=local.kongyu.spark.task.core.MySparkCoreTask \
    --method=run
```

## Spark On YARN

配置hive文件

> 如果Spark配置了Hive，则需要将**hive-site.xml**配置文件拷贝到$SPARK_HOME/conf下

```shell
ln -s $HIVE_HOME/conf/hive-site.xml $SPARK_HOME/conf/hive-site.xml
```

提交任务到yarn（客户端模式），适用于开发测试

> 传入参数指定需要运行的类和方法

```shell
spark-submit \
    --master yarn \
    --deploy-mode client \
    MySparkOnSpringCluster-1.0.jar \
    --class=local.kongyu.spark.task.core.MySparkCoreTask \
    --method=run
```

提交任务到yarn（集群模式），适用于生产

> 传入参数指定需要运行的类和方法

```shell
spark-submit \
    --master yarn \
    --name APP_Spark_on_Spring_Cluster \
    --deploy-mode cluster \
    MySparkOnSpringCluster-1.0.jar \
    --class=local.kongyu.spark.task.core.MySparkCoreTask \
    --method=run
```

## Spark On Kubernetes Operator

参考[文档](https://github.com/kongyu666/work/blob/main/work/bigdata/05-spark/kubernetes-operator/deploy/spark-spring-myapp.yaml)
