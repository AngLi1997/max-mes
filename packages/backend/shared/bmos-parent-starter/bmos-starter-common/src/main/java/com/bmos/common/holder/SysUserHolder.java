package com.bmos.common.holder;


import com.bmos.common.base.user.SysUser;

import java.util.Optional;

public class SysUserHolder {
    private static final ThreadLocal<SysUser> USER = new ThreadLocal<>();

    public static void setUser(SysUser user) {
        USER.set(user);
    }

    public static SysUser getUser() {
        return Optional.ofNullable(USER.get()).orElse(new SysUser(){

            @Override
            public String getUserId() {
                return null;
            }

            @Override
            public void setUserId(String userId) {

            }

            @Override
            public String getLoginName() {
                return null;
            }

            @Override
            public void setLoginName(String loginName) {

            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public void setPassword(String password) {

            }

            @Override
            public String getToken() {
                return null;
            }

            @Override
            public void setToken(String token) {

            }

            @Override
            public Long getLoginTime() {
                return null;
            }

            @Override
            public void setLoginTime(Long loginTime) {

            }

            @Override
            public String getUserName() {
                return null;
            }

            @Override
            public void setUserName(String username) {

            }

            @Override
            public Boolean getActivated() {
                return false;
            }

            @Override
            public void setActivated(Boolean activated) {

            }
        });
    }

    public static void remove() {
        USER.remove();
    }


}
