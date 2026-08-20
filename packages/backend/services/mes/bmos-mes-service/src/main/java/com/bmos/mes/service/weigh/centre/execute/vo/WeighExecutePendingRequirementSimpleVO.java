package com.bmos.mes.service.weigh.centre.execute.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.unit.service.UnitCache;
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
@ApiModel("称量执行待称量需求查询结果")
public class WeighExecutePendingRequirementSimpleVO {

    @ApiModelProperty(value = "称量需求id", example = "1")
    private Long id;

    @ApiModelProperty(value = "配方物料id", example = "1")
    private Long formulaMaterialId;

    @ApiModelProperty(value = "需求量", example = "8.000")
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "ml")
    private String unit;

    @ApiModelProperty(value = "生产批号", example = "RY01001-2406002")
    private String batchNo;

    @ApiModelProperty(value = "产品名称", example = "热轧板")
    private String productName;

    @ApiModelProperty(value = "产品合并码", example = "RHB")
    private String productMergeCode;

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
