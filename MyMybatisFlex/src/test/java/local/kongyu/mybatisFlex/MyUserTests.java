package local.kongyu.mybatisFlex;

import com.github.javafaker.Faker;
import local.kongyu.mybatisFlex.entity.MyUser;
import local.kongyu.mybatisFlex.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 生成测试数据到数据库
 *
 * @author 孔余
 * @since 2024-01-12 09:42
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyUserTests {
    private final MyUserService myUserService;

    @Test
    void initData() {
        //生成测试数据
        // 创建一个Java Faker实例，指定Locale为中文
        Faker faker = new Faker(new Locale("zh-CN"));
        // 创建一个包含不少于100条JSON数据的列表
        List<MyUser> myUserList = new ArrayList();
        for (int i = 1; i <= 10000; i++) {
            MyUser myUser = new MyUser();
            myUser.setName(faker.name().fullName());
            myUser.setBirthday(faker.date().birthday());
            myUser.setAge(faker.number().numberBetween(0, 100));
            myUser.setProvince(faker.address().state());
            myUser.setCity(faker.address().cityName());
            myUser.setScore(faker.number().randomDouble(3, 1, 100));
            myUserList.add(myUser);
        }
        boolean result = myUserService.saveBatch(myUserList);
        System.out.println(result);
    }

    @Test
    void list() {
        List<MyUser> list = myUserService.list();
        System.out.println(list);
    }

    @Test
    void count() {
        long count = myUserService.count();
        System.out.println(count);
    }

}
