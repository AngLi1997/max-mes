package com.bmos.platform.service.material.vo;

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
@ApiModel("物料分类分页查询")
public class MaterialPageVO {


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
     * 已下发业务
     */
    @ApiModelProperty("已下发业务")
    private String dispenseRecord;

    /**
     * 是否是成员物料
     */
    @ApiModelProperty("是否是成员物料")
    private Boolean subMaterial;


    @ApiModelProperty("启停状态")
    private Boolean status;

    @ApiModelProperty("物料分类id")
    private Long materialCategoryId;

    @ApiModelProperty("所属物料id")
    private Long principalMaterialId;

    @ApiModelProperty("备注")
    private String remark;


}
