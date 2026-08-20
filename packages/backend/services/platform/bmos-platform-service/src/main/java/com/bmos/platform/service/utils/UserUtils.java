package com.bmos.platform.service.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.cache.redis.RedisService;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.auth.constant.BmosRedisKeyDefine;

import java.util.Optional;

public class UserUtils {

    private static PlatformApiAdaptor platformApiAdaptor;

    private static RedisService redisService;

    public static void init(PlatformApiAdaptor platformApiAdaptor, RedisService redisService) {
        UserUtils.platformApiAdaptor = platformApiAdaptor;
        UserUtils.redisService = redisService;
    }

    public static String getUsername(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return null;
        }
        //todo 换缓存key
        BaseUserDO user = (BaseUserDO) redisService.get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNotNull(user)) {
            return user.getUserName();
        }
        return Optional.ofNullable(platformApiAdaptor.getUser(userId)).orElse(new UserInfoVO()).getUserId();
    }

    public static BaseUserDO getUser(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return null;
        }
        //todo 换缓存key
        BaseUserDO user = (BaseUserDO) redisService.get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNotNull(user)) {
            return user;
        }
        return Optional.ofNullable(UserConverter.INSTANCE.convertVO(platformApiAdaptor.getUser(userId))).orElse(new BaseUserDO());

    }
}
