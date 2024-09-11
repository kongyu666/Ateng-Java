package local.kongyu.kafka.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 推送数据到Kafka
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-08-07 16:21:14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 100)
    public void send() {
        String topic = "ateng_springboot_string";
        String key = DateUtil.now();
        String data = JSONObject.of(
                "id", IdUtil.fastSimpleUUID(),
                "dateTime", DateUtil.date()
        ).toJSONString();
        kafkaTemplate.send(topic, key, data);
        //log.info("发送消息到Kafka topic: {}, key: {}, data: {}", topic, key, data);
    }
}
