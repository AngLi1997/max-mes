package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("批记录模板")
public class TemplateSaveDTO extends TemplateFileDTO {

    /**
     * 模板分类id
     */
    @ApiModelProperty("模板分类id")
    @NotNull
    private Long categoryId;

    /**
     * 模板分类名称
     */
    @ApiModelProperty("模板名称")
    @NotEmpty
    private String name;

    /**
     * 模板版本
     */
    @ApiModelProperty(value = "模板版本", required = true)
    @NotEmpty
    private String version;

    /**
     * 授权的部门id集合
     */
    @ApiModelProperty("授权的部门id集合")
    @NotEmpty
    private List<Long> deptIds;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
