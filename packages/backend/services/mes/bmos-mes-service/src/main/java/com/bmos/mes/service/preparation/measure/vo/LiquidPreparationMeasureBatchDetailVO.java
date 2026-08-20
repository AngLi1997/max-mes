package com.bmos.mes.service.preparation.measure.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mes.common.enums.preparation.MeasureStageEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("配液量取批次详情VO")
@Data
public class LiquidPreparationMeasureBatchDetailVO {

    @ApiModelProperty("量取组件实例id")
    private Long instanceId;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料批次编号")
    private String materialBatchNo;

    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    /**
     * 该配液批次已添物料件物料量之和
     */
    @ApiModelProperty("物料总量")
    private BigDecimal totalQuantity;

    @ApiModelProperty("目标量")
    private BigDecimal targetQuantity = BigDecimal.ZERO;

    @ApiModelProperty("已量取")
    private BigDecimal measuredQuantity = BigDecimal.ZERO;

    @ApiModelProperty("未量取")
    private BigDecimal unmeasuredQuantity = BigDecimal.ZERO;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("允差范围")
    private BigDecimal[] toleranceDiff;

    @ApiModelProperty("允差上限")
    private BigDecimal toleranceUpper;

    @ApiModelProperty("允差下限")
    private BigDecimal toleranceLower;

    @ApiModelEnumProperty(value = "允差类型", enumClass = ToleranceTypeEnum.class)
    private ToleranceTypeEnum toleranceType;


    /**
     * 量取阶段
     */
    @ApiModelEnumProperty(value = "量取阶段", enumClass = MeasureStageEnum.class)
    private MeasureStageEnum measureStage;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    private String measurerId;

    /**
     * 称量人名称
     */
    @ApiModelProperty(value = "称量人名称", example = "张三")
    private String measurerName;

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
