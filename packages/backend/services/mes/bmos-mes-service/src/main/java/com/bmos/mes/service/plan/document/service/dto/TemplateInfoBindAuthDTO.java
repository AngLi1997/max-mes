package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 模板信息绑定数据权限DTO
 */
@Getter
@Setter
@ApiModel("模板信息绑定数据权限DTO")
@NoArgsConstructor
@AllArgsConstructor
public class TemplateInfoBindAuthDTO {

    /**
     * 模板信息id
     */
    @ApiModelProperty(value = "模板信息id", required = true)
    @NotNull
    private Long templateInfoId;

    /**
     * 部门权限id
     */
    @ApiModelProperty(value = "部门权限id集合", required = true)
    private List<Long> deptIds;

}
