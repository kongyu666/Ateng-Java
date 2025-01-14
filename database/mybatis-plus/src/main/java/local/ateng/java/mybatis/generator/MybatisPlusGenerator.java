package local.ateng.java.mybatis.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;


/**
 * 代码生成器
 * 全新的 MyBatis-Plus 代码生成器，通过 builder 模式可以快速生成你想要的代码，快速且优雅，跟随下面的代码一睹为快。
 * https://baomidou.com/guides/new-code-generator/
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @since 2025-01-13
 */
public class MybatisPlusGenerator {
    // 生成的包路径
    private static final String BasePackage = "local.ateng.java.mybatis";
    // 需要生成的表
    private static final List<String> GenerateTable = Arrays.asList(
            "my_user", "my_order"
    );

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.1.10:35725/kongyu", "root", "Admin@123")
                .globalConfig(builder -> builder
                        .author("孔余")
                        .outputDir(getModulePath() + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent(BasePackage)
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .addInclude(GenerateTable) // 设置需要生成的表名
                        .entityBuilder() // Entity 策略配置
                        .enableLombok() // 启用 Lombok
                        .enableTableFieldAnnotation() // 启用字段注解
                        .controllerBuilder()// Controller 策略配置
                        .enableRestStyle() // 启用 REST 风格
                )
                .execute();
    }

    /**
     * 获取当前模块的路径
     *
     * @return
     */
    public static String getModulePath() {
        // 获取当前类的路径
        String path = null;
        try {
            path = MybatisPlusGenerator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        // 获取父目录（模块目录）
        File moduleDir = new File(path).getParentFile();
        return moduleDir.getPath().replace("\\target", "");
    }
}