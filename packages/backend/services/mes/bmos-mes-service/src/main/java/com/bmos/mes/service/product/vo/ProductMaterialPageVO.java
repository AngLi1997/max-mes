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
@ApiModel("物料分页VO")
public class ProductMaterialPageVO {


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

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("分类id")
    private Long materialCategoryId;

    @ApiModelProperty("全分类名称")
    private String fullCategoryName;

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

    /**
     * 拓展单位id
     */
    @ApiModelProperty("拓展单位id")
    private Long unitExtendId;

    /**
     * 拓展单位名称
     */
    @ApiModelProperty("拓展单位名称")
    private String unitExtendName;

    /**
     * 是否是成员物料
     */
    @ApiModelProperty("是否是成员物料")
    private Boolean subMaterial;


    @ApiModelProperty("启停状态")
    private Boolean status;


}
