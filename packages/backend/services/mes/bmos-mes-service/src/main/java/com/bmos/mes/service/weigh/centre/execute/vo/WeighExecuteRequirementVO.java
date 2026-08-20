package com.bmos.mes.service.weigh.centre.execute.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighProcess;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighStatusEnum;
import com.bmos.unit.service.UnitCache;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 称量执行需求查询结果
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 15:28
 */
@Data
@ApiModel("称量执行需求查询结果")
public class WeighExecuteRequirementVO {

    @ApiModelProperty(value = "称量需求id", example = "1")
    private Long id;

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    @ApiModelProperty(value = "需求量", example = "8.000")
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "已称量", example = "8.000")
    private BigDecimal weighed;

    @ApiModelProperty(value = "未称量", example = "8.000")
    private BigDecimal unWeighed;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "ml")
    private String unit;

    @ApiModelProperty(value = "产品名称", example = "PBS缓冲液")
    private String productName;

    @ApiModelProperty(value = "产品编码", example = "RY01001")
    private String productMergeCode;

    @ApiModelProperty(value = "生产批号", example = "RY01001-2406002")
    private String batchNo;

    @ApiModelProperty(value = "工艺流程名称", example = "PBS缓冲液配置")
    private String processName;

    private RequirementWeighProcess weighProcess;

    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long storageMaterialBatchId;

    @ApiModelEnumProperty(value = "任务称量状态", enumClass = RequirementWeighStatusEnum.class)
    private RequirementWeighStatusEnum weighStatus;

    @ApiModelEnumProperty(value = "需求状态", enumClass = RequirementStatusEnum.class)
    private RequirementStatusEnum requirementStatus;

    @ApiModelProperty(value = "失效日期", example = "2024-07-10")
    private LocalDate expiredDate;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
