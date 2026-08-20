package com.bmos.mes.service.weigh.centre2.requirement.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.unit.service.UnitCache;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 称量需求列表VO
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/20 10:10
 */
@Data
@ApiModel("称量需求列表VO")
public class WeighRequirementListVO {

    @ApiModelProperty(value = "需求ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料名称", example = "乳糖")
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "37A")
    private String materialMergeCode;

    @ApiModelProperty(value = "物料规格", example = "25kg/袋")
    private String materialSpecification;

    @ApiModelProperty(value = "物料批次", example = "11111111")
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "物料批次ID", example = "1")
    private Long storageMaterialBatchId;

    @ApiModelProperty(value = "称量中心", example = "C3-3车间称量中心")
    private String weighCentreName;

    @ApiModelProperty(value = "需求量", example = "300.000")
    private BigDecimal formulaQuantity;

    @ApiModelProperty(value = "单位ID", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "袋")
    private String unit;

    @ApiModelProperty(value = "产品名称", example = "爱哥列特片")
    private String productName;

    @ApiModelProperty(value = "产品编码", example = "C01001")
    private String productMergeCode;

    @ApiModelProperty(value = "批号", example = "CPX0022309")
    private String batchNo;

    @ApiModelProperty(value = "计划生产日期", example = "2025-05-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planDate;

    @ApiModelProperty(value = "需求用途", example = "制粒使用")
    private String requirementUsage;

    @ApiModelProperty(value = "备注", example = "嘿嘿嘿嘿嘿")
    private String remark;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}