package local.kongyu.gracefulResponse.controller;

import com.feiniaojin.gracefulresponse.api.ValidationStatusCode;
import local.kongyu.gracefulResponse.config.MyValidationGroups;
import local.kongyu.gracefulResponse.entity.UserInfo;
import local.kongyu.gracefulResponse.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 10:22
 */
@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class UserInfoController {
    private final UserInfoService userInfoService;

    @GetMapping("/get")
    @ValidationStatusCode(code = "1314")
    public UserInfo get(
            @NotNull(message = "id不能为空") Long id,
            @NotNull(message = "name不能为空") Long name
    ) {
        log.info("id=用户{}={}", id, name);
        UserInfo user = userInfoService.getUser(id);
        return user;
    }

    @PostMapping("/add")
    public void add(@RequestBody @Validated UserInfo userInfo) {
        log.info("新增用户={}", userInfo);
    }

    @GetMapping("/test1")
    public String test1() {
        int i = 1 / 0;
        System.out.println(i);
        return "okkk";
    }

    @GetMapping("/test2")
    public String test2() {
        ArrayList<Integer> list = new ArrayList<>();
        System.out.println(list.get(111));
        return "okkk";
    }

    @GetMapping("/test3")
    public String test3() {
        ArrayList<Integer> list = null;
        System.out.println(list.get(111));
        return "okkk";
    }

    @GetMapping("/test4")
    public String test4() {
        System.out.println(Integer.valueOf("aaaa"));
        return "okkk";
    }

    @GetMapping("/list")
    public List<UserInfo> list() {
        ArrayList<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo(1L, "1111", 22, "111", "111"));
        list.add(new UserInfo(1L, "1111", 22, "111", "111"));
        list.add(new UserInfo(1L, "1111", 22, "111", "111"));
        return list;
    }

    // 分组效验
    @PostMapping("/add2")
    public String add2(@RequestBody @Validated(MyValidationGroups.Create.class) UserInfo userInfo) {
        System.out.println(userInfo);
        return "okkk";
    }

    @GetMapping("/add3")
    public String add3(@Validated({MyValidationGroups.Create.class, MyValidationGroups.Update.class}) UserInfo userInfo) {
        System.out.println(userInfo);
        return "okkk";
    }
    @GetMapping("/add4")
    public String add4(@Validated({MyValidationGroups.Update.class}) UserInfo userInfo) {
        System.out.println(userInfo);
        return "okkk";
    }

}
