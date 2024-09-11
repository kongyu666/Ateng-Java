package local.kongyu.mybatisFlexPostGis;

import local.kongyu.mybatisFlexPostGis.entity.MyUserEntity;
import local.kongyu.mybatisFlexPostGis.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MyUserTests {
    private final MyUserService myUserService;

    @Test
    void listUser() {
        List<MyUserEntity> list = myUserService.listUser();
        System.out.println(list);
    }

    @Test
    void listPageUser() {
        List<MyUserEntity> myUserEntityList = myUserService.listPageUser(1, 20);
        System.out.println(myUserEntityList);
    }

    @Test
    void getUser() {
        MyUserEntity myUserEntity = myUserService.getUser(100);
        System.out.println(myUserEntity);
    }

    @Test
    void countUser() {
        long userCount = myUserService.count();
        System.out.println(userCount);
    }

    @Test
    void saveUser() {
        MyUserEntity user = new MyUserEntity();
        user.setName("孔余");
        user.setAge(24);
        user.setScore(new BigDecimal("999.99"));
        user.setBirthday(new Date());
        user.setProvince("重庆市");
        user.setCity("重庆市");
        boolean result = myUserService.save(user);
        System.out.println(user);
    }

}
