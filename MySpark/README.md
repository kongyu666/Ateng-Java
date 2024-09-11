# Spark 开发

## 快速开始

### 创建表

```sql
-- 创建表
CREATE TABLE user
(
    id BIGINT,
    uuid STRING,
    name STRING,
    region STRING,
    age INT,
    email STRING,
    salary INT,
    score DOUBLE,
    date_time TIMESTAMP
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',' -- 设置CSV文件中的字段分隔符为逗号
STORED AS TEXTFILE;

-- 导入数据
LOAD DATA INPATH 'hdfs://bigdata01/data/my_table_user.csv' INTO TABLE user;
```

### 提交程序

```shell
$ cat run.sh
#!/bin/bash
set -x

CLASS_NAME="${1:-local.kongyu.mySpark.core.MyRDD}"
DEPLOY_MODE="${2:-client}"

spark-submit \
    --master yarn \
    --name app_${CLASS_NAME} \
    --class ${CLASS_NAME} \
    --deploy-mode ${DEPLOY_MODE} \
    MySpark-1.0.jar

$ chmod +x run.sh
$ ./run.sh local.kongyu.mySpark.core.MyRDD cluster
```

### 查看日志

进入Spark History Server查看日志

```shell
http://bigdata01:18080/
```



## 创建表

### 创建数据库

```sql
-- 创建数据库并设置hdfs存储位置和注释
CREATE DATABASE IF NOT EXISTS my_database
COMMENT 'This is a sample database for demonstration purposes.'
LOCATION 'hdfs://bigdata01:8020/hive/warehouse/my_database';
-- 查看数据库信息
DESCRIBE DATABASE my_database;
-- 切换到my_database
use my_database;
```

### 创建普通表

TEXTFILE 存储格式

> TEXTFILE 是一种简单的文本存储格式，每行都是纯文本，适合存储人类可读的数据。TEXTFILE 格式适用于存储非结构化和文本数据，但通常在大数据环境中不如列式存储格式效率高。

```sql
-- 创建表
DROP TABLE IF EXISTS user_textfile;
CREATE TABLE IF NOT EXISTS user_textfile (
    id BIGINT,
    name STRING
) COMMENT 'User Information'
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
STORED AS TEXTFILE;
-- 查看信息
DESCRIBE EXTENDED user_textfile;
-- 插入数据
INSERT INTO user_textfile VALUES
    (1, 'John'),
    (2, 'Alice'),
    (3, 'Bob');
-- 查看数据
select * from user_textfile;
```

PARQUET 存储格式

> PARQUET 是一种列式存储格式，旨在提供高性能、高效的数据压缩和查询性能。PARQUET 通常在大数据分析场景中被广泛使用，因为它支持高效的列式存储和压缩，适用于快速分析查询。

```sql
-- 创建表
DROP TABLE IF EXISTS user_rarquet;
CREATE TABLE IF NOT EXISTS user_rarquet (
    id BIGINT,
    name STRING
) COMMENT 'User Information'
STORED AS PARQUET;
-- 查看信息
DESCRIBE EXTENDED user_rarquet;
-- 插入数据
INSERT INTO user_rarquet VALUES
    (1, 'John'),
    (2, 'Alice'),
    (3, 'Bob');
-- 查看数据
select * from user_rarquet;
```

### 创建外部表

外部表（External Table）是数据库中的一种表，其数据存储在数据库管理系统之外的外部位置，而不是被数据库系统直接管理。在大多数数据库系统中，外部表与普通表（托管表或内部表）不同，其数据不存储在数据库的默认位置，而是存储在用户指定的路径或外部存储系统中。

```sql
-- 创建表
DROP TABLE IF EXISTS user_external;
CREATE EXTERNAL TABLE IF NOT EXISTS user_external (
    id BIGINT,
    name STRING
) COMMENT 'User Information'
LOCATION '/hive/external/user_external'
STORED AS PARQUET;
-- 查看信息
DESCRIBE EXTENDED user_external;
-- 插入数据
INSERT INTO user_external VALUES
    (1, 'John'),
    (2, 'Alice'),
    (3, 'Bob');
-- 查看数据
select * from user_external;
```

### 创建分区表

> 分区表是一种数据库表的组织方式，其中数据根据某个或某些列的值被划分成多个分区，每个分区存储一组具有相同或相近特征的数据。这种组织结构有助于提高查询性能、简化数据管理，并在某些情况下减少存储成本。

根据id创建分区表

```sql
-- 创建表
DROP TABLE IF EXISTS user_partitioned;
CREATE TABLE IF NOT EXISTS user_partitioned (
    id BIGINT,
    name STRING
) COMMENT 'User Information'
PARTITIONED BY (department_id INT)
STORED AS PARQUET;
-- 查看信息
DESCRIBE EXTENDED user_partitioned;
-- 插入数据
INSERT INTO user_partitioned PARTITION (department_id = 104) VALUES
    (1, 'John'),
    (2, 'Alice'),
    (3, 'Bob');
-- 查看数据
select * from user_partitioned where department_id=104;
-- 查看分区
SHOW PARTITIONS user_partitioned;
```

根据日期创建分区表

```sql
-- 创建表
DROP TABLE IF EXISTS user_partitioned2;
CREATE TABLE IF NOT EXISTS user_partitioned2 (
    id BIGINT,
    name STRING
) COMMENT 'User Information'
PARTITIONED BY (date_partition STRING)
STORED AS PARQUET;
-- 查看信息
DESCRIBE EXTENDED user_partitioned2;
-- 插入数据
INSERT INTO user_partitioned2 PARTITION (date_partition = "2024-01-31") VALUES
    (1, 'John'),
    (2, 'Alice'),
    (3, 'Bob');
-- 查看数据
select * from user_partitioned2 where date_partition="2024-01-31";
-- 查看分区
SHOW PARTITIONS user_partitioned2;
```



### 创建聚簇(分桶)表

聚簇表（Clustered Table）是一种数据库表的组织方式，它在物理存储层面上根据某个或某些列的值对数据进行聚集（Clustering）。聚簇表的目的是将具有相似或相关值的行物理上存储在一起，以提高查询性能和降低磁盘 I/O 操作。

```sql
-- 创建表
DROP TABLE IF EXISTS user_clustered;
CREATE TABLE IF NOT EXISTS user_clustered (
    id BIGINT,
    name STRING
) COMMENT 'User Information'
CLUSTERED BY (id) INTO 5 BUCKETS
STORED AS PARQUET;
-- 查看信息
DESCRIBE EXTENDED user_clustered;
-- 插入数据
INSERT INTO user_clustered VALUES
    (1, 'John'),
    (2, 'Alice'),
    (3, 'Bob'),
    (4, 'Eva'),
    (5, 'Kas');
-- 查看数据
select * from user_clustered;
```



### 创建表时指定表的附加属性

```sql
-- 创建表
DROP TABLE IF EXISTS user_properties;
CREATE TABLE IF NOT EXISTS user_properties (
    id BIGINT,
    name STRING
) COMMENT 'User Information'
TBLPROPERTIES ("created_by"="admin", "created_on"="2024-01-31")
STORED AS PARQUET;
-- 查看信息
DESCRIBE EXTENDED user_properties;
-- 插入数据
INSERT INTO user_properties VALUES
    (1, 'John'),
    (2, 'Alice'),
    (3, 'Bob');
-- 查看数据
select * from user_properties;
```



## 创建外部关系型数据库表

### MySQL

```sql
-- 创建表
DROP TABLE IF EXISTS user_ext_mysql;
CREATE TABLE IF NOT EXISTS user_ext_mysql
USING org.apache.spark.sql.jdbc
OPTIONS (
  driver "com.mysql.cj.jdbc.Driver",
  url "jdbc:mysql://192.168.1.10:35725/kongyu",
  dbtable "user",
  user 'root',
  password 'Admin@123'
);
-- 查看信息
DESCRIBE EXTENDED user_ext_mysql;
-- 插入数据
INSERT INTO user_ext_mysql 
VALUES (8888,'a4770d5c844a4f338510a738958b531f','李强','宁夏回族自治区',24,'blankenshipwilliam@example.org',26206,62.23,CAST('1997-04-21 11:35:17' AS TIMESTAMP));
-- 查看数据
select * from user_ext_mysql;
```

### PostgreSQL

```
-- 创建表
DROP TABLE IF EXISTS user_ext_postgresql;
CREATE TABLE IF NOT EXISTS user_ext_postgresql
USING org.apache.spark.sql.jdbc
OPTIONS (
  driver "org.postgresql.Driver",
  url "jdbc:postgresql://192.168.1.10:32297/kongyu",
  dbtable "public.user",
  user 'postgres',
  password 'Lingo@local_postgresql_5432'
);
-- 查看信息
DESCRIBE EXTENDED user_ext_postgresql;
-- 插入数据
INSERT INTO user_ext_postgresql 
VALUES (8888,'a4770d5c844a4f338510a738958b531f','李强','宁夏回族自治区',24,'blankenshipwilliam@example.org',26206,62.23,CAST('1997-04-21 11:35:17' AS TIMESTAMP));
-- 查看数据
select * from user_ext_postgresql;
```

