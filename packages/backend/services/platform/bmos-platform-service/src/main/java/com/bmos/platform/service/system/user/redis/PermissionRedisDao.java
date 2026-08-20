package com.bmos.platform.service.system.user.redis;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.cache.redis.RedisService;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.auth.constant.BmosRedisKeyDefine;
import com.bmos.platform.service.system.user.convert.UserConvert;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PermissionRedisDao {

    @Autowired
    private RedisService redisService;

    @Autowired
    @Lazy
    private UserMapper userMapper;


    public void cacheUserInfo(User user) {
        redisService.set(user.getToken(), user.getUserId(), BmosRedisKeyDefine.USER_TOKEN_ID_CACHE);
        redisService.set(user.getUserId(), user, BmosRedisKeyDefine.USER_INFO_CACHE);
    }

    public User getUserInfo(String userId) {
        Object user = redisService.get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNull(user)) {
            user = userMapper.selectByUserId(userId);
        }
        return UserConvert.INSTANCE.convertSysUser((BaseUserDO) user);
    }

    public String hasLogin(String userId, String loginTerminalType) {
        return (String) redisService.getHash(userId, loginTerminalType, BmosRedisKeyDefine.USER_LOGIN_CACHE);
    }
    public BaseUserDO getBaseUseInfo(String userId) {
        Object user = redisService.get(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
        if (ObjectUtil.isNull(user)) {
            return null;
        }
        return (BaseUserDO) user;
    }

    public void setUserInfo(BaseUserDO userDO) {
        redisService.set(userDO.getUserId(), userDO, BmosRedisKeyDefine.USER_INFO_CACHE);
    }

    public void batchSetUserInfo(List<BaseUserDO> userDOs) {
        Map<String, String> map = userDOs.stream()
                .collect(Collectors.toMap(baseUserDO -> BmosRedisKeyDefine.USER_INFO_CACHE.formatKey(baseUserDO.getUserId()),
                        JsonUtils::toJsonString, (t1, t2) -> t1));
        redisService.batchSet(map);
    }

    public void removeToken(String token) {
        redisService.delete(token, BmosRedisKeyDefine.USER_TOKEN_ID_CACHE);
    }

    public void removeUserInfo(String userId) {
        redisService.delete(userId, BmosRedisKeyDefine.USER_INFO_CACHE);
    }

    public void removeLoginInfo(String userId,String loginTerminalType) {
        redisService.deleteHash(userId, BmosRedisKeyDefine.USER_LOGIN_CACHE,loginTerminalType);
    }

    public void setUserLogin(String userId, Integer loginTerminalType,String token) {
        redisService.setHash(userId, String.valueOf(loginTerminalType), BmosRedisKeyDefine.USER_LOGIN_CACHE, token);
    }

    public void cacheTokenIp(String token,String ip){
        redisService.set(token, ip, BmosRedisKeyDefine.USER_TOKEN_IP_CACHE);
    }

    public String getTokenIp(String token){
        return (String) redisService.get(token, BmosRedisKeyDefine.USER_TOKEN_IP_CACHE);
    }

    public void removeTokenIp(String token) {
        redisService.delete(token, BmosRedisKeyDefine.USER_TOKEN_IP_CACHE);
    }
}
