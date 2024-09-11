package local.kongyu.mybatisFlex;

import com.github.javafaker.Faker;
import local.kongyu.mybatisFlex.entity.MyUserEntity;
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
public class InitDbDataTests {
    private final MyUserService myUserService;

    @Test
    void initData() {
        //生成测试数据
        // 创建一个Java Faker实例，指定Locale为中文
        Faker faker = new Faker(new Locale("zh-CN"));
        // 创建一个包含不少于100条JSON数据的列表
        List<MyUserEntity> myUserEntityList = new ArrayList();
        for (int i = 1; i <= 10000; i++) {
            MyUserEntity myUserEntity = new MyUserEntity();
            myUserEntity.setName(faker.name().fullName());
            myUserEntity.setBirthday(faker.date().birthday());
            myUserEntity.setAge(faker.number().numberBetween(0, 100));
            myUserEntity.setProvince(faker.address().state());
            myUserEntity.setCity(faker.address().cityName());
            myUserEntity.setScore(faker.number().randomDouble(3, 1, 100));
            myUserEntityList.add(myUserEntity);
        }
        boolean result = myUserService.saveBatch(myUserEntityList);
        System.out.println(result);
    }
}
