package com.bmos.mes.service.ingredient.weigh.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighProcess;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 15:07
 */
@Data
@ApiModel("配料称量批次信息详情")
public class WeighStorageMaterialBatchVO {

    /**
     * 配料计划id
     */
    @ApiModelProperty(value = "配料计划id", example = "1")
    private Long ingredientPlanId;

    /**
     * 称量物料批次id
     */
    @ApiModelProperty(value = "称量物料批次id", example = "1")
    private Long storageMaterialBatchId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String storageMaterialName;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "CNA")
    private String storageMaterialCode;

    /**
     * 配方物料精度
     */
    @ApiModelProperty(value = "配方物料精度", example = "0.0001")
    private BigDecimal scale;

    /**
     * 物料批次编号
     */
    @ApiModelProperty(value = "物料批次编号", example = "123456")
    private String storageMaterialBatchNo;

    /**
     * 物料总量(该配料批次已添加物料件的物料量之和)
     */
    @ApiModelProperty("物料总量(该配料批次已添加物料件的物料量之和)")
    private BigDecimal consumeTotalQuantity;

    /**
     * 目标量(该配料批次的配料称量总目标量（配料量），来源为配料计划)
     */
    @ApiModelProperty("目标量(该配料批次的配料称量总目标量（配料量），来源为配料计划)")
    private BigDecimal targetTotalQuantity = BigDecimal.ZERO;

    /**
     * 已称量
     */
    @ApiModelProperty("已称量")
    private BigDecimal weighedQuantity = BigDecimal.ZERO;

    /**
     * 未称量
     */
    @ApiModelProperty("未称量")
    private BigDecimal unWeighedQuantity = BigDecimal.ZERO;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位i", example = "kg")
    private String unit;

    /**
     * 配料允差类型
     */
    @ApiModelEnumProperty(value = "配料允差类型", enumClass = ToleranceTypeEnum.class)
    @EnumValidate(ToleranceTypeEnum.class)
    private ToleranceTypeEnum toleranceTypeEnum;

    /**
     * 配料允差上限
     */
    @ApiModelProperty(value = "配料允差上限", example = "2")
    private BigDecimal maxTolerance = BigDecimal.ZERO;

    /**
     * 配料允差下限
     */
    @ApiModelProperty(value = "配料允差下限", example = "2")
    private BigDecimal minTolerance = BigDecimal.ZERO;

    /**
     * 允差范围 【允差下限，标准，允差上限】
     */
    @ApiModelProperty(value = "允差范围 【允差下限，标准，允差上限】")
    public BigDecimal[] toleranceDiff;

    /**
     * 余料允差类型
     */
    @ApiModelEnumProperty(value = "余料允差类型", enumClass = ToleranceTypeEnum.class)
    @EnumValidate(ToleranceTypeEnum.class)
    private ToleranceTypeEnum oddToleranceTypeEnum;

    /**
     * 余料允差上限
     */
    @ApiModelProperty(value = "余料允差上限", example = "2")
    private BigDecimal oddMaxTolerance;

    /**
     * 余料允差下限
     */
    @ApiModelProperty(value = "余料允差下限", example = "2")
    private BigDecimal oddMinTolerance;

    /**
     * 余料允差范围 【允差下限，标准，允差上限】
     */
    @ApiModelProperty(value = "余料允差范围 【允差下限，标准，允差上限】")
    private BigDecimal[] oddToleranceDiff;

    /**
     * 配料称量列表
     */
    @ApiModelProperty(value = "配料称量列表")
    private List<IngredientWeighStorageMaterialVO> ingredientList = new ArrayList<>();

    /**
     * 余料称量列表
     */
    @ApiModelProperty(value = "余料称量列表")
    private List<IngredientWeighStorageMaterialVO> oddList = new ArrayList<>();

    /**
     * 称量阶段
     */
    @ApiModelEnumProperty(value = "称量阶段", enumClass = WeighProcess.class)
    @EnumValidate(WeighProcess.class)
    private WeighProcess weighProcess;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    /**
     * 称量人名称
     */
    @ApiModelProperty(value = "称量人名称", example = "张三")
    private String weigherName;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    /**
     * 复核人名称
     */
    @ApiModelProperty(value = "复核人名称", example = "张三")
    private String reCheckerName;

    /**
     * 物料类型
     */
    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum categoryType;
}
