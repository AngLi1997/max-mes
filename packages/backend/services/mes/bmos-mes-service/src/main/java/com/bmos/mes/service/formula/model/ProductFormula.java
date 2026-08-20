package com.bmos.mes.service.formula.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 产品配方
 */
@Getter
@Setter
@TableName("bm_product_formula")
public class ProductFormula extends BaseDO {

    /**
     * 配方名称
     */
    private String name;


    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品合并编码
     */
    private String productMergeCode;

    /**
     * 产品规格
     */
    private String productSpecification;

    /**
     * 单位id
     */
    private Long unitId;

    @ApiModelProperty("版本号")
    @TableField(exist = false)
    private String version;

}
