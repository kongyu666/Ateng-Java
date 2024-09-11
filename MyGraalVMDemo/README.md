# 在Linux系统上编译二进制文件

1. 安装GraalVM JDK 21和Maven 3.9.6
2. 安装编译软件包

```shell
yum -y install gcc binutils zlib-devel
```

3. 编译

```shell
mvn -Pnative -e -X native:compile-no-fork -DskipTests
# 或者
mvn -Pnative native:compile -DskipTests
```

4. 查看并运行可执行文件

```shell
ll target/graalvm
./target/graalvm
```