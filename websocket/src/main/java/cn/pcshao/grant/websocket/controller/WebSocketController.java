package cn.pcshao.grant.websocket.controller;

import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.websocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author pcshao.cn
 * @date 2019-03-21
 */
@Component
@ServerEndpoint(value = "/ws/{topic}")
public class WebSocketController extends BaseController {

    /**
     * 连接事件 加入注解
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam(value = "topic") String topic, Session session) {
        logger.debug("新连接加入");
        WebSocketUtil.addSession(topic, session);
    }

    @OnClose
    public void onClose(@PathParam(value = "topic") String topic, Session session) {
        logger.debug("连接断开");
        WebSocketUtil.removeSession(topic);
    }

    /**
     * 可以使用@SendTo发给其他topic
     * 可以包装message，在message里封装发件人、收件人
     * @param topic
     * @param message
     */
    @OnMessage
    public void OnMessage(@PathParam(value = "topic") String topic, String message) {
        String info = "topic[" + topic + "]：" + message;
        logger.debug(info);
        WebSocketUtil.broadCast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("异常:", throwable);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }

}