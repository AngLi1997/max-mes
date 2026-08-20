package com.bmos.platform.service.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 物料
 */
@Getter
@Setter
@ToString
@ApiModel("物料编辑DTO")
public class MaterialUpdateDTO {

    @ApiModelProperty(value = "id",required = true)
    @NotNull
    private Long id;

    /**
     * 物料分类id
     */
    @ApiModelProperty(value = "物料分类id",required = true)
    @NotNull
    private Long materialCategoryId;

    /**
     * 所属物料id
     */
    @ApiModelProperty("所属物料id")
    private Long principalMaterialId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称",required = true)
    @NotBlank
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码",required = true)
    @NotBlank
    private String code;


    @ApiModelProperty(value = "分类编码",required = true)
    @NotBlank
    private String categoryCode;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码",required = true)
    @NotBlank
    private String specification;


    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id",required = true)
    @NotNull
    private Long unitId;


    /**
     * 是否是成员物料
     */
    @ApiModelProperty(value = "是否是成员物料",required = true)
    @NotNull
    private Boolean subMaterial;


    @ApiModelProperty(value = "备注")
    private String remark;

}
