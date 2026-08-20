package com.bmos.adaptor.config;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.constant.RequestConstant;
import com.bmos.common.holder.SysUserHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        SysUser user = SysUserHolder.getUser();
        if (ObjectUtil.isNull(user)) {
            return;
        }
        requestTemplate.header(RequestConstant.BMOS_TOKEN, user.getToken());
        requestTemplate.header("Content-type", "application/json;charset=UTF-8");
    }
}
