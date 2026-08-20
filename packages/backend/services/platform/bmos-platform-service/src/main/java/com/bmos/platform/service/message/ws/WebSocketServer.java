package com.bmos.platform.service.message.ws;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.cache.redis.RedisService;
import com.bmos.common.constant.RequestConstant;
import com.bmos.common.constant.SecurityConstant;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.jwt.JwtUtils;
import com.bmos.platform.facade.auth.constant.BmosRedisKeyDefine;
import com.bmos.platform.service.message.dto.MessageCountDTO;
import com.bmos.platform.service.message.persistence.IMessagePersistence;
import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WebSocketServer extends TextWebSocketHandler {

    @Autowired
    private RedisService redisService;


    @Autowired
    private IMessagePersistence messagePersistence;

    /**
     * 每一个ws连接都携带当前用户的token，简历连接时验证token的有效期
     * <p>
     * 连接建立时将当前用户的未读数量发送给前端
     *
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String clientId = getClientId(session);
        log.info("建立websocket连接,clientId:{}", clientId);
        WebSocketSessionManager.addSession(clientId, session);
        // 发送当前用户的未读数量
        Map<String, List<MessageCountDTO>> notReadCount = messagePersistence.selectNotReadCount(Lists.newArrayList(getUserId(session)));
        sendMessage(Lists.newArrayList(getUserId(session)), notReadCount);
    }

    private String getClientId(WebSocketSession session) {
        String userId = getUserId(session);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(session.getUri());
        MultiValueMap<String, String> queryParams = uriComponentsBuilder.build().getQueryParams();
        String clientId = queryParams.getFirst("clientId");
        return userId + "#" + clientId;
    }

    // TODO: 2025/1/13 国际化
    private void sendUserNotLoginMessage(WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage(JSONUtil.toJsonStr(ResponseInfo.failure(BaseResponseCode.UN_AUTHORIZATION))));
    }


    // TODO: 2025/1/7 如果这里抛异常不行的话需要发送一个未登录的message给前端
    private String getUserId(WebSocketSession session) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(session.getUri());
        MultiValueMap<String, String> queryParams = uriComponentsBuilder.build().getQueryParams();
        String token = queryParams.getFirst(RequestConstant.BMOS_TOKEN);
        Claims claims = JwtUtils.parseToken(token);
        String loginToken = claims.get(SecurityConstant.LOGIN_TOKEN, String.class);
        Object value = redisService.get(loginToken, BmosRedisKeyDefine.USER_TOKEN_ID_CACHE);
        if (ObjectUtil.isEmpty(value)) {
            try {
                this.sendUserNotLoginMessage(session);
                session.close(CloseStatus.NOT_ACCEPTABLE);
            } catch (Exception e) {
                log.error("发送未登录消息失败", e);
            }
        }
        return (String) value;
    }

    /**
     * 每一个ws请求都需要携带一个token，验证token是否有效，如果token已经失效，则断开连接
     * <p>
     * 客户端发送消息来的时候，验证token是否有效，如果token已经失效，则断开连接
     *
     * @param session session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages from client if needed
        String clientId = getClientId(session);
        log.info("收到websocket消息客户端id：{}，message:{}", clientId, message.getPayload());
        System.out.println("Received message: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String clientId = getClientId(session);
        log.info("关闭websocket连接,客户端id：{}", clientId);
        WebSocketSessionManager.removeSession(clientId);
    }


    public void sendMessage(Collection<String> userIds, Object content) {
        List<WebSocketSession> userSession = WebSocketSessionManager.getUserSession(userIds);
        userSession.forEach(session -> {
            String clientId = getClientId(session);
            ResponseInfo<Object> responseInfo = ResponseInfo.success(content);
            log.info("客户端id：{}，发送消息：{}", clientId, responseInfo);
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(JSONUtil.toJsonStr(responseInfo)));
                } catch (IOException e) {
                    log.error("客户端id：{}，发送消息：{}失败", clientId, responseInfo, e);
                }
            }
        });
    }
}
