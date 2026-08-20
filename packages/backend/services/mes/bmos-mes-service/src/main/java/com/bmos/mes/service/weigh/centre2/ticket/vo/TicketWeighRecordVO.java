package com.bmos.mes.service.weigh.centre2.ticket.vo;

import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/23 18:06
 */
@Data
@ApiModel(description = "称量详情记录VO")
public class TicketWeighRecordVO {

    @ApiModelProperty(value = "需求ID", example = "123456")
    private Long requirementId;

    @ApiModelProperty(value = "产品名称", example = "维生素C片")
    private String productName;

    @ApiModelProperty(value = "产品合并编码", example = "P20250523001")
    private String productMergeCode;

    @ApiModelProperty(value = "批次号", example = "BN20250523001")
    private String batchNo;

    @ApiModelProperty(value = "物料名称", example = "维生素C")
    private String materialName;

    @ApiModelProperty(value = "物料规格", example = "100g")
    private String materialSpecification;

    @ApiModelProperty(value = "物料批次号", example = "BN20250523001")
    private String materialBatchNo;

    @ApiModelProperty(value = "合并编码", example = "P20250523001")
    private String mergeCode;

    @ApiModelProperty(value = "需求数量", example = "100.5")
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "需求用途", example = "生产用")
    private String requirementUsage;

    @ApiModelProperty(value = "单位ID", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位名称", example = "千克")
    private String unit;

    @ApiModelEnumProperty(value = "状态", enumClass = RequirementStatusEnum.class)
    private RequirementStatusEnum status;

    @ApiModelProperty(value = "称量物料记录列表")
    private List<TicketWeighMaterialRecordVO> list;
}
