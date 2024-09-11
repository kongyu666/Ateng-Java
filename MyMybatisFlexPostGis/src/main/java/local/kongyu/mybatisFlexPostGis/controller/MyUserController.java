package local.kongyu.mybatisFlexPostGis.controller;

import local.kongyu.mybatisFlexPostGis.entity.MyUserEntity;
import local.kongyu.mybatisFlexPostGis.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-11 21:53
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyUserController {
    private final MyUserService myUserService;

    @GetMapping("/list")
    public List<MyUserEntity> list() {
        List<MyUserEntity> myUserEntities = myUserService.listUser();
        return myUserEntities;
    }
}
