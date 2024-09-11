package local.kongyu.stomp.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务类，用于管理WebSocket连接和用户信息。
 * 该类包含处理WebSocket连接和断开事件的方法，以及获取已连接客户端信息的方法。
 *
 * 作者：孔余
 * 日期：2024-05-11 17:35
 */
@Component
public class WebSocketService {
    // 保存已连接的客户端信息的映射
    private final Map<String, StompHeaderAccessor> connectedClients = new ConcurrentHashMap<>();
    // 保存已连接的用户ID和会话ID的映射
    private final Map<String, String> userSessionMap = new ConcurrentHashMap<>();

    /**
     * 处理WebSocket连接事件的方法。
     * 当有新的WebSocket连接建立时，将会话ID和用户ID添加到对应的映射中。
     *
     * @param event WebSocket连接事件
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String userId = accessor.getFirstNativeHeader("userId"); // 获取用户ID

        // 将会话ID和StompHeaderAccessor对象保存到connectedClients中
        connectedClients.put(sessionId, accessor);
        // 将会话ID和用户ID保存到userSessionMap中
        userSessionMap.put(sessionId, userId);
    }

    /**
     * 处理WebSocket断开连接事件的方法。
     * 当WebSocket连接断开时，从connectedClients和userSessionMap中移除对应的映射关系。
     *
     * @param event WebSocket断开连接事件
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        // 从connectedClients中移除对应的客户端信息
        connectedClients.remove(sessionId);
        // 从userSessionMap中移除对应的用户信息
        userSessionMap.remove(sessionId);
    }

    /**
     * 获取已连接客户端信息的方法。
     *
     * @return 已连接客户端信息的映射
     */
    public Map<String, StompHeaderAccessor> getConnectedClients() {
        return connectedClients;
    }

    /**
     * 获取已连接用户ID和会话ID的映射的方法。
     *
     * @return 已连接用户ID和会话ID的映射
     */
    public Map<String, String> getConnectedUsers() {
        return userSessionMap;
    }
}
