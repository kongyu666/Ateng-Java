package local.kongyu.kafka;

import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.Charset;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MySpringKafkaApplicationTests {
    private final KafkaTemplate kafkaTemplate;

    @Test
    void contextLoads() {
        String str = FileUtil.readString("D:\\Work\\高新三期\\kafka数据\\GD_ListInterUnbalance_topic.json", Charset.defaultCharset());
        kafkaTemplate.send("test", str);
    }

}
