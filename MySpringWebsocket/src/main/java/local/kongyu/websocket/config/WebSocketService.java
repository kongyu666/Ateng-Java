package local.kongyu.websocket.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * WebSocket服务类，用于管理WebSocket会话和消息发送。
 * 该类包含了添加和移除WebSocket会话、统计会话信息、向所有用户或指定用户发送消息的方法。
 *
 * 作者：孔余
 * 日期：2024-05-15 11:26
 */
@Component
public class WebSocketService {

    private static final Logger logger = Logger.getLogger(WebSocketService.class.getName());

    // 保存WebSocket会话的映射
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    // 保存用户ID和会话ID的映射
    private final Map<String, String> userSessionMap = new ConcurrentHashMap<>();

    /**
     * 添加WebSocket会话的方法。
     *
     * @param session WebSocket会话
     * @param user    用户ID
     */
    public void addSession(WebSocketSession session, String user) {
        sessions.put(session.getId(), session);
        userSessionMap.put(session.getId(), user);
    }

    /**
     * 移除WebSocket会话的方法。
     *
     * @param sessionId 会话ID
     */
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
        userSessionMap.remove(sessionId);
    }

    /**
     * 统计每个用户的会话数量。
     *
     * @return 用户ID和会话数量的映射
     */
    public Map<String, Long> getSessionByUser() {
        // 计算每个值在Map中出现的次数
        return userSessionMap.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * 记录会话信息的方法。
     */
    public void logSessionInfo() {
        logger.log(Level.INFO, "当前连接总数: " + sessions.size() + ", 连接信息：" + getSessionByUser());
    }

    /**
     * 向所有连接的客户端发送消息的方法。
     *
     * @param message 要发送的消息
     */
    public void sendMessageToAll(String message) {
        for (WebSocketSession session : sessions.values()) {
            sendMessageIfSessionOpen(session, message);
        }
    }

    /**
     * 向指定用户发送消息的方法。
     *
     * @param userId  用户ID
     * @param message 要发送的消息
     */
    public void sendMessageToUser(String userId, String message) {
        userSessionMap.forEach((key, value) -> {
            if (userId.equals(value)) {
                sendMessageIfSessionOpen(sessions.get(key), message);
            }
        });
    }

    /**
     * 如果会话是打开状态，则向会话发送消息。
     *
     * @param session WebSocket会话
     * @param message 要发送的消息
     */
    private void sendMessageIfSessionOpen(WebSocketSession session, String message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "发送消息失败: " + e.getMessage());
            }
        }
    }

}
