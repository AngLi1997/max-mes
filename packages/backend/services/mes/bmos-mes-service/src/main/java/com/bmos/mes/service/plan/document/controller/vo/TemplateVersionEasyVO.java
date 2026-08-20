package com.bmos.mes.service.plan.document.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板版本VO
 */
@Getter
@Setter
@ApiModel("批记录模板版本VO")
public class TemplateVersionEasyVO {

    /**
     * 版本id
     */
    @ApiModelProperty("版本id")
    private Long id;

    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private String version;

}
