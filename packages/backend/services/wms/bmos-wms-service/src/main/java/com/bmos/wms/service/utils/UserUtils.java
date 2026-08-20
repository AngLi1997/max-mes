package com.bmos.wms.service.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.cache.redis.RedisService;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.auth.constant.BmosRedisKeyDefine;

import java.util.Optional;

public class UserUtils {

    private static PlatformApiAdaptor platformApiAdaptor;

    private static RedisService redisService;

    public static String getUsername(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return null;
        }
        BaseUserDO user = (BaseUserDO) getRedisService().get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNotNull(user)) {
            return user.getUserName();
        }
        return Optional.ofNullable(getPlatformApiAdaptor().getUser(userId)).orElse(new UserInfoVO()).getUserId();
    }

    public static BaseUserDO getUser(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return null;
        }
        BaseUserDO user = (BaseUserDO) getRedisService().get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNotNull(user)) {
            return user;
        }
        return Optional.ofNullable(UserConverter.INSTANCE.convertVO(getPlatformApiAdaptor().getUser(userId))).orElse(new BaseUserDO());

    }

    private static PlatformApiAdaptor getPlatformApiAdaptor() {
        if (platformApiAdaptor == null) {
            platformApiAdaptor = SpringUtil.getBean(PlatformApiAdaptor.class);
        }
        return platformApiAdaptor;
    }

    private static RedisService getRedisService() {
        if (redisService == null) {
            redisService = SpringUtil.getBean(RedisService.class);
        }
        return redisService;
    }
}
