package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产线DTO
 */
@Getter
@Setter
@ApiModel("产线DTO")
public class LineSaveDTO {

    /**
     * 产线编码
     */
    @ApiModelProperty("产线编码")
    @NotBlank
    private String code;

    /**
     * 产线名称
     */
    @ApiModelProperty("产线名称")
    @NotBlank
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @NotBlank
    private String description;

    /**
     * 模型id
     */
    @ApiModelProperty("模型id")
    @NotNull
    private Long moduleId;

    /**
     * 产线数据权限所属部门
     */
    @ApiModelProperty("部门id集合")
    private List<Long> deptIds;

}
