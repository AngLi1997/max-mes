package com.bmos.adaptor.platform.dto;

public class ValidatePwd {

    /**
     * 旧密码
     */
    private String loginName;

    /**
     * 新密码
     */
    private String password;

    public ValidatePwd(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
