package local.kongyu.mybatisFlex;

import local.kongyu.mybatisFlex.entity.MyOrderEntity;
import local.kongyu.mybatisFlex.service.MyOrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-12 11:08
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyOrderTests {
    private final MyOrderService myOrderService;

    @Test
    void listOrder() {
        List<MyOrderEntity> list = myOrderService.listOrder();
        System.out.println(list);
    }
}
