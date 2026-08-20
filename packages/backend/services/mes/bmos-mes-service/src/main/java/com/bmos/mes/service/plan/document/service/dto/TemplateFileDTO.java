package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ApiModel("批记录模板文件")
public class TemplateFileDTO {

    /**
     * 模板base64
     */
    @ApiModelProperty(value = "模板路径", required = true)
    @NotEmpty
    private String path;

}
