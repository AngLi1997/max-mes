package com.bmos.platform.facade.system.execute.parameter.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("BusinessParameterDetailVO:参数配置详情VO")
public class BusinessParameterDetailFeignVO {
    /**
     * 编码
     */
    private String code;

    /**
     * 值
     */
    private String value;
}
