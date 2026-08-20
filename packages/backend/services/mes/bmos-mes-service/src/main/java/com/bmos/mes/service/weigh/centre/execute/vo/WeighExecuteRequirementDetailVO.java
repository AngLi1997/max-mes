package com.bmos.mes.service.weigh.centre.execute.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighProcess;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighProcess;
import com.bmos.unit.service.UnitCache;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 称量执行需求详情查询结果
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 15:28
 */
@Data
@ApiModel("称量执行需求详情查询结果")
public class WeighExecuteRequirementDetailVO {

    @ApiModelProperty(value = "称量需求id", example = "1")
    private Long id;

    @ApiModelProperty(value = "配方物料id", example = "1", hidden = true)
    private Long formulaMaterialId;

    @ApiModelProperty(value = "物料id", example = "1", hidden = true)
    private Long materialId;

    @ApiModelProperty(value = "物料id", example = "1")
    private String materialName;

    @ApiModelProperty(value = "物料合并编码", example = "CNA")
    private String materialMergeCode;

    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum categoryType;

    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long storageMaterialBatchId;

    @ApiModelProperty(value = "物料批次编号", example = "123456")
    private String storageMaterialBatchNo;

    @ApiModelProperty("物料总量(该批次已添加物料件的物料量之和)")
    private BigDecimal batchConsumeTotalQuantity;

    @ApiModelProperty(value = "物料总量(该需求已添加物料件的物料量之和)", hidden = true)
    @JsonIgnore
    private BigDecimal consumeTotalQuantity;

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    @ApiModelProperty(value = "生产批号", example = "CNA")
    private String batchNo;

    @ApiModelProperty(value = "产品名称", example = "CNA")
    private String productName;

    @ApiModelProperty(value = "目标量(该物料需求的总目标量)", example = "CNA")
    private BigDecimal targetTotalQuantity;

    @ApiModelProperty(value = "已称量(该物料需求的已称量的重量)", example = "CNA")
    private BigDecimal weighedQuantity;

    @ApiModelProperty(value = "未称量(该物料需求的未称量的重量, 目标量减去已称量)", example = "CNA")
    private BigDecimal unWeighedQuantity;

    @ApiModelProperty(value = "剩余量(物料批次的物料总量减去该物料批次已称量)")
    private BigDecimal remainingQuantity;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位i", example = "kg")
    private String unit;

    @ApiModelEnumProperty(value = "称量阶段", enumClass = WeighProcess.class)
    @EnumValidate(RequirementWeighProcess.class)
    private RequirementWeighProcess weighProcess;

    @ApiModelProperty("允差信息")
    private WeighExecuteDiff diff;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }

    @ApiModelProperty(value = "称量人员id", example = "1")
    private String weigherId;

    @ApiModelProperty(value = "任务id", example = "1")
    private Long taskId;
}
