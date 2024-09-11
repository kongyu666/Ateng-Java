package local.kongyu.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nacos配置中心测试接口
 *
 * @author 孔余
 * @since 2024-05-29 09:03
 */
@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Demo!";
    }


}
