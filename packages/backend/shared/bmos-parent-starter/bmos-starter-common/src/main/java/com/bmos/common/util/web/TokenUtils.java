package com.bmos.common.util.web;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.constant.RequestConstant;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    public static String getToken(HttpServletRequest request) {
        // 从请求头获取
        String token = request.getHeader(RequestConstant.BMOS_TOKEN);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        // 从cookie获取
        token = CookieUtils.getCookieValue(request, RequestConstant.COOKIE_HEAD_TOKEN);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        // 从urlParam中获取
        return null;
    }
}
