package com.bmos.mes.service.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 物料
 */
@Getter
@Setter
@ToString
@ApiModel("物料详情VO")
public class ProductMaterialDetailVO {


    @ApiModelProperty("id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 合并编码
     */
    @ApiModelProperty("合并编码")
    private String mergeCode;

    /**
     * 所属物料id
     */
    @ApiModelProperty("所属物料id")
    private Long principalMaterialId;

    /**
     * 临期提醒天数
     */
    @ApiModelProperty("临期提醒天数")
    private Integer dyingPeriod;

    /**
     * 保存条件
     */
    @ApiModelProperty("保存条件")
    private String storageCondition;


    /**
     * 物料分类id
     */
    @ApiModelProperty("物料分类id")
    private Long materialCategoryId;


    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String specification;


    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("拓展单位id")
    private Long unitExtendId;

    @ApiModelProperty("拓展单位名称")
    private String unitExtendName;

    /**
     * 是否是成员物料
     */
    @ApiModelProperty("是否是成员物料")
    private Boolean subMaterial;

    /**
     * 是否是成品
     */
    @ApiModelProperty("是否是成品")
    private Boolean finishProduct;


    @ApiModelProperty("启停状态")
    private Boolean status;

    @ApiModelProperty(value = "生产周期(天)")
    private Integer productionCycle;

    @ApiModelProperty(value = "内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty(value = "包装规格")
    private String packingSpecification;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty(value = "拓展信息")
    private MaterialExpandInfoVO expandInfo;

}
