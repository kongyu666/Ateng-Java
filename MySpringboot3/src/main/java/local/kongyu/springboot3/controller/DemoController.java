package local.kongyu.springboot3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-16 11:07
 */
@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {
    @GetMapping("/hello")
    public String demo() {
        var str = """
                hello world!
                kongyu
                """;
        log.info("线程：{}", Thread.currentThread());
        return "hello world!";
    }
}
