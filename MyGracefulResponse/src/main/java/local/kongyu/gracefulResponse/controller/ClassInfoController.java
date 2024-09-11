package local.kongyu.gracefulResponse.controller;

import local.kongyu.gracefulResponse.entity.ClassInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 10:22
 */
@RestController
@RequestMapping("/class")
@Slf4j
@Validated
public class ClassInfoController {

    @PostMapping("/add")
    @ResponseBody
    public String add(@RequestBody @Validated ClassInfo classInfo) {
        log.info("classInfo={}", classInfo);
        return "ok";
    }

    @GetMapping("/get")
    @ResponseBody
    public void get() {
    }

}
