package local.kongyu.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置类，用于配置WebSocket相关的参数和行为。
 * 该类通过实现WebSocketConfigurer接口来配置WebSocket处理器和端点。
 *
 * 作者：孔余
 * 日期：2024-05-09 11:13
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MyWebSocketHandler myWebSocketHandler;

    /**
     * 注册WebSocket处理器和端点的方法。
     *
     * @param registry WebSocket处理器注册对象
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册WebSocket处理器，指定处理器和多个端点
        registry.addHandler(
                myWebSocketHandler,        // 使用自定义的WebSocket处理器
                "/ws",                     // 主端点
                "/ws/demo1",               // 示例端点1
                "/ws/demo2"                // 示例端点2
        ).setAllowedOrigins("*");        // 允许所有来源的请求访问WebSocket端点
    }
}
