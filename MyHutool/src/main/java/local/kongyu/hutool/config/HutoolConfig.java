package local.kongyu.hutool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 17:44
 */
@Configuration
// 扫描cn.hutool.extra.spring包下所有类并注册之
@ComponentScan(basePackages={"cn.hutool.extra.spring"})
public class HutoolConfig {
    @Bean(name = "myBean")
    public String myBean() {
        System.out.println(11111);
        return "myBean";
    }
}
