package com.bmos.platform.service.message.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @className: WebSocketSessionManager
 * @author: yigaohui
 * @date: 2025/1/7 9:43
 * @Version: 1.0
 * @description:
 */

@Slf4j
public class WebSocketSessionManager {
    private static final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    // 添加一个新的 WebSocket 会话
    public static void addSession(String clientId, WebSocketSession session) {
        sessionMap.put(clientId, session);
    }

    // 移除一个 WebSocket 会话
    public static void removeSession(String clientId) {
        sessionMap.remove(clientId);
    }

    // 根据会话 ID 获取一个 WebSocket 会话
    public static WebSocketSession getSession(String clientId) {
        return sessionMap.get(clientId);
    }

    public static List<WebSocketSession> getUserSession(Collection<String> userIds) {
        return sessionMap.entrySet().stream().filter(entry -> userIds.contains(entry.getKey().split("#")[0])).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    // 发送消息给所有连接的会话
    public static void sendMessageToAll(String message) {
        for (WebSocketSession session : sessionMap.values()) {
            try {
                session.sendMessage(new org.springframework.web.socket.TextMessage(message));
            } catch (Exception e) {
                log.error("发送消息失败", e);
            }
        }
    }
}
