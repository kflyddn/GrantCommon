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
    private static final String processName = "process";
    private static final Map<String, Session> processSessionMap = new ConcurrentHashMap<>();

    public static void addSession(String topic, Session session) {
      sessionMap.putIfAbsent(topic, session);
      if(processName.equals(topic)){
          processSessionMap.putIfAbsent(topic, session);
      }
    }

    public static void remoteSession(String topic) {
        sessionMap.remove(topic);
        if(processName.equals(topic)){
            processSessionMap.remove(topic);
        }
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
    public static void broadCast(Map<String, Session> inputSessionMap, String message) {
        inputSessionMap.forEach((sessionId, session) -> sendMessage(session, message));
    }

    public static Map<String, Session> getSessionMap() {
        return sessionMap;
    }

    public static Map<String, Session> getProcessSessionMap() {
        return processSessionMap;
    }
}
