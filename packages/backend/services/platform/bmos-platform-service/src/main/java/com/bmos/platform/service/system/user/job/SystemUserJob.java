package com.bmos.platform.service.system.user.job;

public interface SystemUserJob {

    /**
     * 密码有效期校验
     */
    void pwdExpireValid();

    /**
     * 账号锁定时间到了之后自动解锁
     * 当前定时器每分钟执行一次
     * 若配置的时间为0，则可能会有1min的延迟
     */
    void userUnLockExpireValid();

}
