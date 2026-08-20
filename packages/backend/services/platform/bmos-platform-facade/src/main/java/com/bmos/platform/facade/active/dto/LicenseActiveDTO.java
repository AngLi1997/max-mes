package com.bmos.platform.facade.active.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 激活码校验开关
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseActiveDTO {

    /**
     * 是否需要启用激活码功能校验
     */
    private Boolean active;

    /**
     * 有效期
     */
    private String date;

}
