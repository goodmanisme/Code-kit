package com.cloud.cloud.opentelemetry.exporter.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/trace/exporter/ws")
@Slf4j
public class WebSocketServer {

    private Session session;
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    public static CopyOnWriteArraySet<WebSocketServer> wsServerSet = new CopyOnWriteArraySet<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        wsServerSet.add(this);
        // 在线数加1
        onlineCount.incrementAndGet();
        log.info(">>>>>>>有新客户端加入:{},当前在线客户端数:{}",session.getId(),onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        wsServerSet.remove(this);
        // 在线数减1
        onlineCount.decrementAndGet();
        log.info(">>>>>>>有客户端退出:{},当前在线客户端数:{}",session.getId(),onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message,Session session) {
        log.info(">>>>>>>服务端接收到客户端[{}]的消息:{}",session.getId(),message);
        this.sendMessage(message);
    }

    /**
     * @param error
     */
    @OnError
    public void onError(Session session,Throwable error) {
        log.info(">>>>>>>客户端:{},连接失败.",session.getId());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message)  {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
