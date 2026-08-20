package com.bmos.platform.service.system.user.constant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 密码校验
 */
@Getter
@Setter
@ToString
public class PasswordValidate {

    /**
     * 密码有效天数
     */
    private Integer pwdValidDates = 365;
}
