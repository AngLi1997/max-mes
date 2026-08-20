package com.bmos.platform.facade.auth.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.active.ActiveApiAdaptor;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.cache.redis.RedisService;
import com.bmos.common.exception.ActiveException;
import com.bmos.common.exception.AuthorizationException;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.web.TokenUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.active.dto.LicenseActiveDTO;
import com.bmos.platform.facade.active.dto.LicenseParamDTO;
import com.bmos.platform.facade.auth.constant.BmosRedisKeyDefine;
import com.bmos.platform.facade.auth.feign.ActiveValidFeign;
import com.bmos.platform.facade.auth.properties.BmosAuthProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenValidateInterceptor implements HandlerInterceptor {
    private final static Logger log = LoggerFactory.getLogger(TokenValidateInterceptor.class);

    private final BmosAuthProperties bmosAuthProperties;

    private final PlatformApiAdaptor platformApiAdaptor;

    private final RedisService redisService;

    private ActiveApiAdaptor activeApiAdaptor;

    private ActiveValidFeign activeValidFeign;

    @Value("${spring.application.name}")
    private String applicationName;

    public TokenValidateInterceptor(BmosAuthProperties bmosAuthProperties,
                                    PlatformApiAdaptor platformApiAdaptor,
                                    RedisService redisService,
                                    ActiveApiAdaptor activeApiAdaptor,
                                    ActiveValidFeign activeValidFeign) {
        this.bmosAuthProperties = bmosAuthProperties;
        this.platformApiAdaptor = platformApiAdaptor;
        this.redisService = redisService;
        this.activeApiAdaptor = activeApiAdaptor;
        this.activeValidFeign = activeValidFeign;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //option方法放过
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }
        for (String exclude : bmosAuthProperties.getActiveUrls()) {
            if (antPathMatcher.match(exclude, request.getRequestURI())) {
                return true;
            }
        }
        validateActiveCode();
        for (String exclude : bmosAuthProperties.getExcludeUrls()) {
            if (antPathMatcher.match(exclude, request.getRequestURI())) {
                return true;
            }
        }
        if (!bmosAuthProperties.isEnable()) {
            return true;
        }
        // 获取tokenActivate.java
        String token = TokenUtils.getToken(request);
        if (StrUtil.isEmpty(token)) {
            throw new AuthorizationException();
        }
        // 首先尝试从本地缓存中获取登录用户信息
        Object value = redisService.get(token, BmosRedisKeyDefine.USER_TOKEN_ID_CACHE);
        if (ObjectUtil.isEmpty(value)) {
            throw new AuthorizationException();
        }
        String userId = (String) value;
        BaseUserDO sysUser = (BaseUserDO) redisService.get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNull(sysUser)) {
            UserInfoVO user = platformApiAdaptor.getUser(userId);
            if (ObjectUtil.isNull(user)) {
                log.error("用户不存在： {}", user.getUserId());
                throw new AuthorizationException();
            }
            sysUser = new BaseUserDO();
            sysUser.setUserId(user.getUserId());
            sysUser.setUserName(user.getUserName());
            sysUser.setLoginName(user.getLoginName());
        }
        try {
            //设置当前登录用户信息
            sysUser.setToken(token);
            SysUserHolder.setUser(sysUser);
            for (String ignoreUrl: bmosAuthProperties.getIgnoreAuthUrls()){
                if (antPathMatcher.match(ignoreUrl, request.getRequestURI())){
                    return true;
                }
            }
            redisService.set(token, userId, BmosRedisKeyDefine.USER_TOKEN_ID_CACHE);
            return true;
        } catch (Exception e) {
            log.error("鉴权失败：{}", e.getCause() + e.getMessage());
            redisService.delete(token, BmosRedisKeyDefine.USER_TOKEN_ID_CACHE);
            throw new BmosException(BaseResponseCode.UN_AUTHORIZATION);
        }
    }

    /**
     * 激活码校验
     */
    private void validateActiveCode() {
        try{
            // 获取平台的active
            String activeCode = activeApiAdaptor.getActiveCode();
            LicenseParamDTO licenseParamDTO = new LicenseParamDTO(activeCode, applicationName);
            ResponseInfo<LicenseActiveDTO> responseInfo = activeValidFeign.activeValid(licenseParamDTO);
            LicenseActiveDTO licenseActiveDTO = responseInfo.getData();
            if (!licenseActiveDTO.getActive()){
                throw new ActiveException();
            }
        } catch (Exception e){
            log.error("调用平台校验激活码失败", e);
            throw new ActiveException();
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SysUserHolder.remove();
    }
}
