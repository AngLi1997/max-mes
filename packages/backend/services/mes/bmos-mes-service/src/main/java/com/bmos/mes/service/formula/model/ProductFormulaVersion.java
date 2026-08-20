package com.bmos.mes.service.formula.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.formula.FormulaVersionStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 产品配方版本
 */
@Getter
@Setter
@TableName("bm_product_formula_version")
public class ProductFormulaVersion extends BaseDO {

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 产品配方id
     */
    private Long productFormulaId;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 状态
     */
    private FormulaVersionStatusEnum status;

    /**
     * 启停状态
     */
    private Boolean enable;

    /**
     * 批量
     */
    private BigDecimal batchQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 流程实例id
     */
    private String processInstanceId;


}
