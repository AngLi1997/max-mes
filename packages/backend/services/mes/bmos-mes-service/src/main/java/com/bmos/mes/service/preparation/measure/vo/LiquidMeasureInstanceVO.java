package com.bmos.mes.service.preparation.measure.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("配液量取组件实例VO")
public class LiquidMeasureInstanceVO {

    /**
     * 组件实例id
     */
    @ApiModelProperty(value = "配液量取组件实例id", example = "1")
    private Long id;
    
    /**
     * 配液计划id
     */
    @ApiModelProperty(value = "配液计划id", example = "1")
    private Long liquidPreparationPlanId;

    /**
     * 量取中的批次id
     */
    @ApiModelProperty(value = "量取批次id", example = "1")
    private Long measuringBatchId;

    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    @ApiModelProperty("配液批次id")
    private Long planBatchId;

    /**
     * 量取中的批次编号
     */
    @ApiModelProperty(value = "量取中的批次编号", example = "1")
    private String measuringBatchNo;

    /**
     * 物料名称(物料合并编码 - 物料名称)
     */
    @ApiModelProperty(value = "物料名称(物料合并编码 - 物料名称)", example = "YH101001 - 盐酸组氨")
    private String materialFullName;

    /**
     * 量取中的批次单位
     */
    @ApiModelProperty(value = "量取中的批次单位", example = "kg")
    private String measuringBatchUnitName;

    /**
     * 量取中的批次单位id
     */
    @ApiModelProperty(value = "量取中的批次单位id", example = "1")
    private Long measuringBatchUnitId;

    /**
     * 配液计划名称
     */
    @ApiModelProperty(value = "配液计划名称", example = "人血白蛋白-2402016-01")
    private String liquidPreparationPlanName;

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id", example = "1")
    private List<Long> station = new ArrayList<>();

}
