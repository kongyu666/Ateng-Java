package local.kongyu.log.controller;

import local.kongyu.log.annotation.OperationLog;
import local.kongyu.log.exception.MyException;
import org.springframework.web.bind.annotation.*;

/**
 * 测试接口
 *
 * @author 孔余
 * @since 2023-03-15 14:16
 */
@RestController
@RequestMapping("/aspect")
public class TestController {
    @GetMapping("/info")
    @OperationLog(operModul = "模块-测试", operType = "查询1", operDesc = "查询信息，正常")
    public String info(@RequestParam("name") String name) {
        return name;
    }

    @PostMapping("/info2")
    @OperationLog(operModul = "模块-测试", operType = "查询1", operDesc = "查询信息，正常")
    public String info2(@RequestBody String name) {
        return name;
    }

    @GetMapping("/error")
    @OperationLog(operModul = "模块-测试", operType = "查询2", operDesc = "查询信息，报错")
    public String error(@RequestParam("name") String name) {
        try {
            int result = 1 / 0;
            System.out.println(result);
        } catch (Exception e) {
            throw new MyException(501, "数据运算错误");
        }
        return name;
    }
}