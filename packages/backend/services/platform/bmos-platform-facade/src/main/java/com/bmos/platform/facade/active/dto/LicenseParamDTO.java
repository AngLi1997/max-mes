package com.bmos.platform.facade.active.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 需要校验的参数
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseParamDTO {
    /**
     * 激活码
     */
    private String activeCode;

    /**
     * 应用名称
     */
    private String applicationName;

}
