package local.kongyu.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket处理器，用于处理WebSocket连接的建立、消息的接收和发送，以及连接的关闭。
 */
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 当WebSocket连接建立后调用的方法。
     *
     * @param session 当前WebSocket会话
     * @throws Exception 如果发生错误
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从连接URL中获取token和user参数
        String token = getParamValueFromUrl(session.getUri().getQuery(), "token");
        String user = getParamValueFromUrl(session.getUri().getQuery(), "user");

        // 校验token是否有效
        if (!isValidToken(token)) {
            // 如果token无效，则发送消息给客户端，并关闭连接
            session.sendMessage(new TextMessage("校验不通过"));
            session.close();
            return;
        }

        // 将会话添加到WebSocketService中管理
        webSocketService.addSession(session, user);

        // 记录会话信息
        webSocketService.logSessionInfo();
    }

    /**
     * 处理收到的文本消息。
     *
     * @param session 当前WebSocket会话
     * @param message 收到的文本消息
     * @throws Exception 如果发生错误
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获取消息内容
        String payload = message.getPayload();

        // 向所有连接的客户端发送消息
        //webSocketService.sendMessageToAll("你好，" + payload);
    }

    /**
     * 当WebSocket连接关闭后调用的方法。
     *
     * @param session 当前WebSocket会话
     * @param status  关闭状态
     * @throws Exception 如果发生错误
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 从WebSocketService中移除关闭的会话
        webSocketService.removeSession(session.getId());

        // 记录会话信息
        webSocketService.logSessionInfo();
    }

    /**
     * 从URL中获取参数值的方法。
     *
     * @param query     URL查询字符串
     * @param paramName 参数名称
     * @return 参数值，如果参数不存在则返回null
     */
    private String getParamValueFromUrl(String query, String paramName) {
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && paramName.equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }

    /**
     * 校验token是否有效的方法。
     *
     * @param token 要校验的token
     * @return 校验结果，true表示有效，false表示无效
     */
    private boolean isValidToken(String token) {
        // 示例中简单地假设所有token都是合法的，实际应用中应根据具体逻辑进行校验
        return "123456".equals(token);
    }
}
