package com.bmos.mes.service.weigh.centre2.requirement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 修改称量工单需求DTO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 20:15
 */
@Data
@ApiModel(value = "修改称量需求组DTO")
public class TicketRequirementGroupEditDTO {

    @ApiModelProperty(value = "需求组id", required = true, example = "1001")
    @NotNull(message = "需求组id不能为空")
    private Long id;

    @ApiModelProperty(value = "产品id", required = true, example = "1001")
    @NotNull(message = "产品id不能为空")
    private Long productId;

    @ApiModelProperty(value = "生产BOM版本id", required = true, example = "2001")
    @NotNull(message = "生产BOM版本id不能为空")
    private Long bomVersionId;

    @ApiModelProperty(value = "生产批号", required = true, example = "PRD202405190001")
    @NotEmpty(message = "生产批号不能为空")
    private String batchNo;

    @ApiModelProperty(value = "称量中心", required = true, example = "1")
    @NotNull(message = "称量中心不能为空")
    private Long centreWeighId;

    @ApiModelProperty(value = "计划生产时间", required = true, example = "2025-05-20")
    @NotNull(message = "计划生产时间不能为空")
    private LocalDate planDate;

    @ApiModelProperty(value = "备注", example = "紧急生产订单")
    private String remark;

    @ApiModelProperty(value = "配方物料批次列表")
    @NotEmpty(message = "物料批次列表不能为空")
    private List<FormulaMaterialBatchDTO> formulaMaterialBatchDTOS;

    /**
     * 物料批次信息
     */
    @Data
    @ApiModel(value = "物料批次信息")
    public static class FormulaMaterialBatchDTO {

        @ApiModelProperty(value = "配方物料id", required = true, example = "3001")
        private Long formulaMaterialId;

        @ApiModelProperty(value = "物料批次信息列表")
        private List<MaterialBatchDTO> batches;
    }

    /**
     * 物料批次信息
     */
    @Data
    @ApiModel(value = "物料批次信息")
    public static class MaterialBatchDTO {
        
        @ApiModelProperty(value = "物料id", required = true, example = "4001")
        @NotNull(message = "物料id不能为空")
        private Long materialId;
        
        @ApiModelProperty(value = "批次id", required = true, example = "5001")
        @NotNull(message = "批次id不能为空")
        private Long storageMaterialBatchId;

        @ApiModelProperty(value = "配方用量", required = true, example = "10.0")
        private BigDecimal formulaQuantity;

        @ApiModelProperty("理论量")
        private BigDecimal theoreticalQuantity;

        @ApiModelProperty(value = "单位id", required = true, example = "6001")
        private Long unitId;
    }
} 