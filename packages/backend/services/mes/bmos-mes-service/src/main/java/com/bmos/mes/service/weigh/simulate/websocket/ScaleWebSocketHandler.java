package com.bmos.mes.service.weigh.simulate.websocket;

import com.bmos.mes.service.weigh.simulate.service.ScaleConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 模拟称重WebSocket处理器
 * 
 * @author system
 * @date 2025-01-26
 */
@Component
@Slf4j
public class ScaleWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ScaleConfigService scaleConfigService;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        
        log.info("WebSocket连接建立，会话ID: {}", sessionId);
        
        // 连接建立后立即发送一个随机数
        sendRandomWeight(session);
        
        // 开始定时发送随机数据
        startPeriodicDataSending(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        
        log.info("WebSocket连接关闭，会话ID: {}", sessionId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("收到客户端消息: {}", message.getPayload());
        
        // 可以根据客户端消息进行不同的处理
        String payload = message.getPayload();
        if ("GET_WEIGHT".equals(payload)) {
            sendRandomWeight(session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误，会话ID: {}", session.getId(), exception);
        sessions.remove(session.getId());
    }

    /**
     * 发送随机重量数据
     * 
     * @param session WebSocket会话
     */
    private void sendRandomWeight(WebSocketSession session) {
        try {
            BigDecimal weight = scaleConfigService.generateRandomWeight();
            session.sendMessage(new TextMessage(weight.toPlainString()));
            
            log.debug("发送称重数据: {}", weight);
            
        } catch (IOException e) {
            log.error("发送称重数据失败", e);
        } catch (Exception e) {
            log.error("生成称重数据失败", e);
        }
    }

    /**
     * 开始定时发送数据
     * 
     * @param session WebSocket会话
     */
    private void startPeriodicDataSending(WebSocketSession session) {
        scheduler.scheduleAtFixedRate(() -> {
            if (session.isOpen()) {
                sendRandomWeight(session);
            } else {
                sessions.remove(session.getId());
            }
        }, 1, 2, TimeUnit.SECONDS); // 每2秒发送一次数据
    }

    /**
     * 广播消息给所有连接的客户端
     * 
     * @param message 消息内容
     */
    public void broadcastMessage(String message) {
        sessions.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                log.error("广播消息失败", e);
            }
        });
    }
} 