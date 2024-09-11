package local.kongyu.stomp.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import local.kongyu.stomp.config.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class MyWebsocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private WebSocketService webSocketService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) {
        log.info("客户端发送了一条消息: {}", message);
        return "Hello, " + message + "!";
    }

    @MessageMapping("/send/{userId}")
    public void sendMessageToUser(@DestinationVariable String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId,"/topic/message", StrUtil.format("{} 服务端定时发送了一条数据：userId={}, message={}", DateUtil.now(),userId,message));
    }

    @GetMapping("/count")
    public Map<String, Long > count() {
        // 计算每个值在Map中出现的次数
        Map<String, Long> valueCount = webSocketService.getConnectedUsers().values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return valueCount;
    }

}
