package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板下载/作废DTO
 */
@Getter
@Setter
@ApiModel("批记录模板版本操作DTO")
public class TemplateVersionOperateDTO {

    /**
     * 批记录模板id
     */
    @ApiModelProperty("批记录模板id")
    private Long templateVersionId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
