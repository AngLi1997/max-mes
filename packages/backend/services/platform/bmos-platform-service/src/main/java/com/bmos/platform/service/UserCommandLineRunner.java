package com.bmos.platform.service;

import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.service.system.user.convert.UserConvert;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.redis.PermissionRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionRedisDao permissionRedisDao;
    @Override
    public void run(String... args) throws Exception {
        List<BaseUserDO> users = userMapper.selectList()
            .stream()
            .map(UserConvert.INSTANCE::convertUserVO2)
            .collect(Collectors.toList());
        permissionRedisDao.batchSetUserInfo(users);
    }
}
