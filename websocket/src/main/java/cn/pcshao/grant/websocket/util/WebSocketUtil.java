package cn.pcshao.grant.websocket.util;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pcshao.cn
 * @date 2019-03-21
 */
public class WebSocketUtil {
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public static void addSession(String userNick,Session session) {
      sessionMap.putIfAbsent(userNick, session);
    }

    public static void remoteSession(String topic) {
        sessionMap.remove(topic);
    }

    public static void sendMessage(Session session, String message) {
        if(session == null) {
            return;
        }
        // getAsyncRemote()和getBasicRemote()异步与同步
        RemoteEndpoint.Async async = session.getAsyncRemote();
        async.sendText(message);
    }

    /**
     * 使用ConcurrentHashMap
     *  也可使用线程池遍历map发，对性能有利
     * @param message
     */
    public static void broadCast(String message) {
        sessionMap.forEach((sessionId, session) -> sendMessage(session, message));
    }
}
