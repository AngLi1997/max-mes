package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("批记录模板版本重新上传DTO")
public class TemplateVersionUpdateDTO extends TemplateFileDTO {

    /**
     * 模板版本id
     */
    @ApiModelProperty(value = "模板版本id", required = true)
    @NotNull
    private Long templateVersionId;

    /**
     * 修改备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
