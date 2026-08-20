package com.bmos.platform.service.execute.parameter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlatformPwdRule {

    /**
     * 是否需要有小写字符
     */
    private Boolean lowerCase;

    /**
     * 是否需要有大写字符
     */
    private Boolean upperCase;

    /**
     * 是否需要有数字
     */
    private Boolean digit;

    /**
     * 特殊字符
     */
    private List<String> specialCharacters;

}
