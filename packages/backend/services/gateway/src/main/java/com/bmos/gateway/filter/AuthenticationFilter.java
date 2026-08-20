package com.bmos.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.bmos.cache.redis.RedisService;
import com.bmos.common.constant.RequestConstant;
import com.bmos.common.constant.SecurityConstant;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.common.util.jwt.JwtUtils;
import com.bmos.gateway.properties.BmosAuthProperties;
import com.bmos.gateway.redis.BmosRedisKeyDefine;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {


    @Autowired
    private BmosAuthProperties authProperties;

    @Autowired
    private RedisService redisService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // option请求直接放过，用于跨域检查的
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }
        if (!authProperties.isEnable()){
            return chain.filter(exchange);
        }
        for (String exclude : authProperties.getExcludeUrls()) {
            if (!antPathMatcher.match(exclude, request.getURI().getPath())) {
                continue;
            }
            return chain.filter(exchange);
        }

        List<String> tokenHeader = request.getHeaders().get(RequestConstant.BMOS_TOKEN);
        if (null == tokenHeader) {
            return webFluxUnauthorizedResponse(exchange.getResponse());
        }

        String token = tokenHeader.stream().findAny()
                .orElseThrow(() -> new BmosException(BaseResponseCode.UN_AUTHORIZATION));

        if (StrUtil.isEmpty(token)){
            return webFluxUnauthorizedResponse(exchange.getResponse());
        }

        Claims claims = JwtUtils.parseToken(token);
        if (claims == null || claims.isEmpty()) {
            return webFluxUnauthorizedResponse(exchange.getResponse());
        }

        String loginToken = claims.get(SecurityConstant.LOGIN_TOKEN, String.class);
        String userId = (String) redisService.get(loginToken, BmosRedisKeyDefine.USER_TOKEN_ID_CACHE);
        if (StrUtil.isEmpty(userId)) {
            return webFluxUnauthorizedResponse(exchange.getResponse());
        }

        if (!userId.equals(claims.get(SecurityConstant.USER_ID, String.class))) {
            return webFluxUnauthorizedResponse(exchange.getResponse());
        }
        String encodeToken;
        try {
            encodeToken = URLEncoder.encode(loginToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodeToken = StrUtil.EMPTY;
        }
        mutate.header(RequestConstant.BMOS_TOKEN, encodeToken);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }


    private Mono<Void> webFluxUnauthorizedResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ResponseInfo<?> result = ResponseInfo.failure(BaseResponseCode.UN_AUTHORIZATION);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtils.toJsonByte(result));
        return response.writeWith(Mono.just(dataBuffer));
    }

}
