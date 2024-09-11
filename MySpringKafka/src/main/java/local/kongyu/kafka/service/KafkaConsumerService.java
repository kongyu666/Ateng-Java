package local.kongyu.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 接收Kafka消息
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-08-07 16:35:45
 */
@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    // 消费单条消息
    @KafkaListener(topics = "ateng_springboot_string")
    public void listen(ConsumerRecord<String, String> record) {
        String key = record.key();
        long timestamp = record.timestamp();
        String value = record.value();
        log.info("Received message: key={}, timestamp={}, value={}", key, timestamp, value);
    }

    // 消费多条消息，条数指定 ${spring.kafka.consumer.max-poll-records}
    /*@KafkaListener(topics = "ateng_springboot_string")
    public void listen(List<ConsumerRecord<String, String>> records) {
        // 处理接收到的消息
        System.out.println(records);
    }*/
}
