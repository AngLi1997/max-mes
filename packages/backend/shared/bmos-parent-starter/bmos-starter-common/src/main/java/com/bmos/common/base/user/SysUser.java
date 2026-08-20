package com.bmos.common.base.user;

public interface SysUser {


    String getUserId();

    void setUserId(String userId);

    String getLoginName();

    void setLoginName(String loginName);

    String getPassword();

    void setPassword(String password);

    String getToken();

    void setToken(String token);

    Long getLoginTime();

    void setLoginTime(Long loginTime);

    String getUserName();

    void setUserName(String userName);

    Boolean getActivated();

    void setActivated(Boolean activated);

    default String isMultiSessionAllowed() {
        return "ALLOWED";
    }

}
