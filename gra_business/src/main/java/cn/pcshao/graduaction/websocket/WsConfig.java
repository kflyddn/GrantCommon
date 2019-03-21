package cn.pcshao.graduaction.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * WebSocket配置类
 *  配置拦截路径
 * @author pcshao.cn
 * @date 2019-03-21
 */
//@Component
//@EnableWebSocket
public class WsConfig implements WebSocketConfigurer {

    @Resource
    private WsHandler websocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(websocketHandler, "/ws");
    }
}
