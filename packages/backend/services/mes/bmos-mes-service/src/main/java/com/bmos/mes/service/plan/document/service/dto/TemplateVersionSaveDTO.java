package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 模板版本保存DTO
 */
@Getter
@Setter
@ApiModel("模板版本保存DTO")
public class TemplateVersionSaveDTO extends TemplateFileDTO {

    /**
     * 模板信息id
     */
    @ApiModelProperty(value = "模板信息id", required = true)
    @NotNull
    private Long templateInfoId;

    /**
     * 模板版本
     */
    @ApiModelProperty(value = "模板版本", required = true)
    @NotEmpty
    private String version;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
