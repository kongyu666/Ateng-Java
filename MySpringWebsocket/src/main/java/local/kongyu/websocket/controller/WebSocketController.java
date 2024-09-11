package local.kongyu.websocket.controller;

// WebSocketController.java

import local.kongyu.websocket.config.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/websocket")
public class WebSocketController {
    @Autowired
    WebSocketService webSocketService;

    @GetMapping("/sessions")
    public Map<String, Long> getWebSocketSessions() {
        // 获取所有 WebSocket 连接的会话 ID
        return webSocketService.getSessionByUser();
    }

    @GetMapping("/send-all")
    public void sendMessageAll() {
        // 获取所有 WebSocket 连接的会话 ID
        webSocketService.sendMessageToAll("Hello Spring!");
    }

    @GetMapping("/send-user")
    public void sendMessageUser() {
        // 获取所有 WebSocket 连接的会话 ID
        webSocketService.sendMessageToUser("1","Hello Spring!");
    }
}

