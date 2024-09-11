package local.kongyu.stomp.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import local.kongyu.stomp.config.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MyWebsocketTasks {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private WebSocketService webSocketService;

    // 给所有用户发送消息
    @Scheduled(fixedRate = 3000)
    public void sendScheduledData() {
        messagingTemplate.convertAndSend("/topic/greetings", StrUtil.format("服务端定时发送了一条数据：{}", DateUtil.now()));
    }

    // 给指定用户发送消息
    @Scheduled(fixedRate = 3000)
    public void sendScheduledDataUser() {
        messagingTemplate.convertAndSendToUser("1","/topic/message", StrUtil.format("服务端定时发送了一条数据：{}", DateUtil.now()));
    }

    // 自定义发送用户消息
    @Scheduled(fixedRate = 3000)
    public void sendScheduledDataCustom() {
        // 获取所有用户数据
        Map<String, String> connectedUsers = webSocketService.getConnectedUsers();
        List<String> userList = connectedUsers.values().stream().distinct().collect(Collectors.toList());
        userList.forEach(user->{
            if ("1".equals(user)) {
                messagingTemplate.convertAndSendToUser(user,"/topic/message", StrUtil.format("管理员：服务端定时发送了一条数据：{}", DateUtil.now()));
            } else {
                messagingTemplate.convertAndSendToUser(user,"/topic/message", StrUtil.format("普通用户：服务端定时发送了一条数据：{}", DateUtil.now()));
            }
        });
    }

}
