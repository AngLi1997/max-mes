package com.bmos.mes.service.formula.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.formula.DryAndPureTypeEnum;
import com.bmos.mes.common.enums.formula.QuantityTypeEnum;
import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品配方物料
 */
@Getter
@Setter
@TableName(value = "bm_product_formula_material", autoResultMap = true)
public class ProductFormulaMaterial extends BaseDO {

    /**
     * 配方版本id
     */
    private Long versionId;

    /**
     * 物料类型
     */
    private CategoryInfoTypeEnum materialType;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialMergeCode;

    /**
     * 物料规格
     */
    private String materialSpecification;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 数量类型
     */
    private QuantityTypeEnum quantityType;

    /**
     * 物料精度
     */
    private BigDecimal scale;

    /**
     * 精度长度
     */
    private int scaleLength;

    /**
     * 修约方式
     */
    private String rounding;

    /**
     * 折干折纯类型
     */
    private DryAndPureTypeEnum dryPureType;

    /**
     * 折干折纯参数
     */
    private BigDecimal dryPureParam;

    /**
     * 拆包允差类型
     */
    private ToleranceTypeEnum unpackingToleranceType;

    /**
     * 拆包允差上限
     */
    private BigDecimal unpackingToleranceUpper;

    /**
     * 拆包允差下限
     */
    private BigDecimal unpackingToleranceLower;

    /**
     * 配料允差类型
     */
    private ToleranceTypeEnum chargeMixtureToleranceType;

    /**
     * 配料允差上限
     */
    private BigDecimal chargeMixtureToleranceUpper;

    /**
     * 配料允差下限
     */
    private BigDecimal chargeMixtureToleranceLower;

    /**
     * 余料允差类型
     */
    private ToleranceTypeEnum oddmentToleranceType;

    /**
     * 余料允差上限
     */
    private BigDecimal oddmentToleranceUpper;

    /**
     * 余料允差下限
     */
    private BigDecimal oddmentToleranceLower;

    /**
     * 允差信息
     */
    @TableField(value = "tolerance_info", typeHandler = JacksonTypeHandler.class)
    private ProductFormulaToleranceInfo toleranceInfo;

    /**
     * 称量需求信息
     */
    @TableField(value = "weigh_requirement_list_json", typeHandler = JacksonTypeHandler.class)
    private List<ProductFormulaWeighRequirementInfo> weighRequirementList = new ArrayList<>();

    public ProductFormulaToleranceInfo getToleranceInfo() {
        if (toleranceInfo == null) {
            return new ProductFormulaToleranceInfo();
        }
        return toleranceInfo;
    }


}
