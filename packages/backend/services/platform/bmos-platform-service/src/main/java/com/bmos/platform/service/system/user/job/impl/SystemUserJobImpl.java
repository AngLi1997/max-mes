package com.bmos.platform.service.system.user.job.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.platform.service.system.user.enums.ActiveEnum;
import com.bmos.platform.service.system.user.job.SystemUserJob;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.service.UserService;
import com.bmos.scheduler.core.handler.annotation.XxlJob;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户相关定时器
 */
@lombok.extern.slf4j.Slf4j
@Service
@Slf4j
public class SystemUserJobImpl implements SystemUserJob {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Override
    @XxlJob("userPwdExpireValid")
    public void pwdExpireValid() {
        userService.pwdExpireValid();
    }

    @Override
    @XxlJob("userUnLockExpireValid")
    public void userUnLockExpireValid() {
        userService.userAutoUnLockExpireValid();
    }
}
