package com.bmos.lims2.server.platform.util;

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

    public static void init(PlatformApiAdaptor platformApiAdaptor,RedisService redisService){
        UserUtils.platformApiAdaptor = platformApiAdaptor;
        UserUtils.redisService = redisService;
    }
    public static String getUsername(String userId) {
        if (StrUtil.isEmpty(userId)){
            return null;
        }
        if ("system".equalsIgnoreCase(userId)) {
            return "system";
        }
        BaseUserDO user = (BaseUserDO) redisService.get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNotNull(user)){
            return user.getUserName();
        }
        return Optional.ofNullable(platformApiAdaptor.getUser(userId)).orElse(new UserInfoVO()).getUserId();
    }

    /**
     * 获取用户展示名称，格式：姓名-登录名
     * 用于页面展示请验人、操作人等需要同时展示姓名和登录名的场景
     */
    public static String getUserDisplayName(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return null;
        }
        if ("system".equalsIgnoreCase(userId)) {
            return "system";
        }
        BaseUserDO user = getUser(userId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    public static BaseUserDO getUser(String userId) {
        if (StrUtil.isEmpty(userId)){
            return null;
        }
        if ("system".equalsIgnoreCase(userId)) {
            return null;
        }
        BaseUserDO user = (BaseUserDO) redisService.get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNotNull(user)){
            return user;
        }
        return Optional.ofNullable(UserConverter.INSTANCE.convertVO(platformApiAdaptor.getUser(userId))).orElse(new BaseUserDO());

    }
}
