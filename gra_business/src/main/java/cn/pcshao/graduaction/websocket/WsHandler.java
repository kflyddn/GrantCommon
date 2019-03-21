package cn.pcshao.graduaction.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author pcshao.cn
 * @date 2019-03-20
 */
@Component
public class WsHandler implements WebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(WsHandler.class);
    @Resource
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;

    public static final Map<Long, WebSocketSession> processSocketSessionMap;  //进度
    public static final Map<Long, WebSocketSession> testSocketSessionMap;

    static {
        processSocketSessionMap = new HashMap<>();
        testSocketSessionMap = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        logger.debug("新连接建立"+ webSocketSession.getId());  //这个ID似乎是实现HandShake可以存进去
        Long id = (Long) webSocketSession.getAttributes().get("id");
        if(null != processSocketSessionMap.get(id)){
            processSocketSessionMap.put(id, webSocketSession);
        }
    }

    /**
     * 向sessionMap广播消息
     * @param textMessage
     * @param sessionMap
     */
    public void brocast(TextMessage textMessage, Map<Long, WebSocketSession> sessionMap){
        Iterator<Map.Entry<Long, WebSocketSession>> iterator = sessionMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Long, WebSocketSession> entry = iterator.next();
            WebSocketSession session = entry.getValue();
            if(session.isOpen()){
                taskExecutor.execute(() ->{
                    try {
                        if(session.isOpen()) {
                            session.sendMessage(textMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        logger.debug("handleMessage");

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        logger.debug("消息处理错误处理");

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        logger.debug("关闭WebSocket连接");

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public static Map<Long, WebSocketSession> getProcessSocketSessionMap() {
        return processSocketSessionMap;
    }

    public static Map<Long, WebSocketSession> getTestSocketSessionMap() {
        return testSocketSessionMap;
    }
}
