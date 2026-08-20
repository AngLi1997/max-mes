package com.bmos.mes.service.weigh.centre2.requirement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建称量工单需求DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 20:15
 */
@Data
@ApiModel(value = "保存称量需求组需求配置DTO")
public class TicketRequirementGroupRequirementDTO {

    @ApiModelProperty(value = "需求组id", required = true)
    @NotNull(message = "需求组id不能为空")
    private Long groupId;

    @ApiModelProperty(value = "配方物料id", required = true)
    @NotNull
    private Long formulaMaterialId;

    @ApiModelProperty(value = "配方需求key", required = true)
    @NotBlank
    private String key;

    @ApiModelProperty(value = "需求物料批次信息列表")
    private List<MaterialBatchDTO> batches = new ArrayList<>();

    @Data
    @ApiModel(value = "需求物料批次信息")
    public static class MaterialBatchDTO {

        @ApiModelProperty(value = "物料id", required = true)
        @NotNull(message = "物料id不能为空")
        private Long materialId;

        @ApiModelProperty(value = "批次id", required = true)
        @NotNull(message = "批次id不能为空")
        private Long storageMaterialBatchId;

        @ApiModelProperty(value = "配料量", required = true)
        @NotNull
        private BigDecimal formulaQuantity;

        @ApiModelProperty(value = "理论量")
        private BigDecimal tempTheoreticalQuantity;

        @ApiModelProperty(value = "单位id", required = true)
        @NotNull
        private Long unitId;
    }
} 