package com.bmos.mes.service.trace.material.vo;

import com.bmos.mes.service.trace.material.entity.PercentYieldRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 11:58
 */
@Data
public class MaterialTraceTree {

    private Long relationId;

    private String materialName;

    private Long materialId;

    private String mergeCode;

    private Long storageMaterialBatchId;

    private String storageMaterialBatchNo;

    private String batchNo;

    private Long productPlanId;

    private BigDecimal consumeQuantity;

    private BigDecimal outputQuantity;

    private Long unitId;

    private String unitName;

    @ApiModelProperty(value = "收率范围")
    private PercentYieldRange percentYieldRange;

    @ApiModelProperty(value = "收率", example = "100")
    private BigDecimal percentYield;

    @ApiModelProperty(value = "下级列表")
    private List<MaterialTraceTree> children = new ArrayList<>();
}
