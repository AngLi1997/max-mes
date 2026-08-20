package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 新建设备分类模型入参
 */
@Getter
@Setter
@ApiModel("新建设备分类入参")
public class CategorySaveDTO {

    /**
     * 父级id 若没有父级则为0
     */
    @ApiModelProperty(value = "父级id", required = true, notes = "若没有父级则为0")
    private Long parentId;

    /**
     * 模型编码
     */
    @ApiModelProperty(value = "设备类型编码", required = true)
    @NotBlank
    private String code;

    /**
     * 模型名称
     */
    @ApiModelProperty(value = "设备类型名称", required = true)
    @NotBlank
    private String name;


}
