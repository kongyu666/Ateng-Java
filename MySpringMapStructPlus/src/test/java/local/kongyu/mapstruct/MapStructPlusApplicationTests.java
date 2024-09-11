package local.kongyu.mapstruct;

import cn.hutool.core.date.DateUtil;
import io.github.linpeilie.Converter;
import local.kongyu.mapstruct.dto.UserDTO;
import local.kongyu.mapstruct.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MapStructPlusApplicationTests {
    @Autowired
    private Converter converter;

    @Test
    void converter01() {
        User user = new User("阿腾", 24, true, DateUtil.date().toTimestamp(), DateUtil.date());
        UserDTO userDTO = converter.convert(user, UserDTO.class);
        System.out.println(userDTO);
    }

    @Test
    void converter02() {
        ArrayList<User> list = new ArrayList<>() {{
            add(new User("阿腾1", 24, true, DateUtil.date().toTimestamp(), DateUtil.date()));
            add(new User("阿腾2", 25, false, DateUtil.date().toTimestamp(), DateUtil.date()));
        }};
        List<UserDTO> dtoList = converter.convert(list, UserDTO.class);
        System.out.println(dtoList);
    }

}
