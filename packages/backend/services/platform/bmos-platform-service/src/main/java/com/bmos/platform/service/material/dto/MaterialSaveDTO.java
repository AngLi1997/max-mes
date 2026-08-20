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
@ApiModel("物料保存DTO")
public class MaterialSaveDTO {

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
     * 是否是主要物料
     */
    @ApiModelProperty(value = "是否是主要物料",required = true)
    @NotNull
    private Boolean subMaterial;


    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "业务注册")
    private boolean businessRegister;

    @ApiModelProperty("业务名称")
    private String businessName;


}
