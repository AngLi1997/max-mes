package com.bmos.wms.service.platform.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("生产物料保存DTO")
public class ProductMaterialSaveDTO {

    /**
     * 物料分类id
     */
    @ApiModelProperty(value = "物料分类id", required = true)
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
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    private String code;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格", required = true)
    @NotBlank
    private String specification;


    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", required = true)
    @NotNull
    private Long unitId;


    @ApiModelProperty(value = "拓展单位id")
    private Long unitExtendId;

    /**
     * 是否是主要物料
     */
    @ApiModelProperty(value = "是否是成员物料/成员产品", required = true)
    @NotNull
    private Boolean subMaterial;

    @ApiModelProperty(value = "是否是成品")
    private Boolean finishProduct;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "生产周期(天)")
    private Integer productionCycle;

    @ApiModelProperty(value = "内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty(value = "包装规格")
    private String packingSpecification;

    @ApiModelProperty(value = "业务注册")
    private boolean businessRegister;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty("拓展信息")
    private MaterialExpandInfo expandInfo;
}
