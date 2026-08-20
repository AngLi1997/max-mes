package com.bmos.mes.service.weigh.centre2.requirement.vo;

import com.bmos.mes.common.enums.formula.DryAndPureTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建称量工单需求DTO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 20:15
 */
@Data
@ApiModel(value = "创建称量需求组详情VO", description = "称量工单需求组详细信息")
public class TicketRequirementGroupInfoVO {

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

    @ApiModelProperty(value = "配方物料批次列表", required = true)
    @NotEmpty(message = "物料批次列表不能为空")
    private List<FormulaMaterialBatchInfo> formulas;

    /**
     * 物料批次信息
     */
    @Data
    @ApiModel(value = "配方物料批次信息", description = "包含配方物料及其批次详情")
    public static class FormulaMaterialBatchInfo {

        @ApiModelProperty(value = "配方物料id", required = true, example = "3001")
        private Long formulaMaterialId;

        @ApiModelProperty(value = "key", required = true, example = "1_1")
        private String key;

        @ApiModelProperty(value = "物料名称", example = "活性炭")
        private String materialName;

        @ApiModelProperty(value = "物料规格", example = "500g/瓶")
        private String materialSpecification;

        @ApiModelEnumProperty(value = "折算方式", enumClass = DryAndPureTypeEnum.class)
        private DryAndPureTypeEnum dryAndPureType;

        @ApiModelProperty(value = "需求目标量", example = "10.5")
        private BigDecimal requirementQuantity;

        @ApiModelProperty(value = "单位id", example = "1")
        private Long unitId;

        @ApiModelProperty(value = "单位名称", example = "kg")
        private String unit;

        @ApiModelProperty(value = "需求用途", example = "主料")
        private String requirementUsage;

        @ApiModelProperty(value = "配方用量", required = true, example = "5.75")
        private BigDecimal formulaQuantity;

        @ApiModelProperty(value = "库存是否充足", example = "true")
        private Boolean enough;

        @ApiModelProperty(value = "物料批次列表")
        private List<MaterialBatchInfo> batches;
    }

    /**
     * 物料批次信息
     */
    @Data
    @ApiModel(value = "物料批次详细信息", description = "特定批次的物料详情")
    public static class MaterialBatchInfo {
        
        @ApiModelProperty(value = "物料id", required = true, example = "2001")
        @NotNull(message = "物料id不能为空")
        private Long materialId;
        
        @ApiModelProperty(value = "批次id", required = true, example = "3001")
        @NotNull(message = "批次id不能为空")
        private Long storageMaterialBatchId;

        @ApiModelProperty(value = "物料批次号", example = "BATCH20250519001")
        private String storageMaterialBatchNo;

        @ApiModelProperty(value = "水分", example = "0.05")
        private BigDecimal hydration;

        @ApiModelProperty(value = "含量", example = "0.95")
        private BigDecimal noHydrationContent;

        @ApiModelProperty(value = "理论量", example = "10.5")
        private BigDecimal theoreticalQuantity;

        @ApiModelProperty(value = "配方用量", required = true, example = "5.75")
        private BigDecimal formulaQuantity;

        @ApiModelProperty(value = "单位id", required = true, example = "1")
        private Long unitId;

        @ApiModelProperty(value = "单位名称", example = "kg")
        private String unit;

        @ApiModelProperty(value = "有效期至", example = "2026-05-11")
        private LocalDate expiredDate;

        @ApiModelProperty(value = "供应商", example = "上海化工有限公司")
        private String supplier;

        @ApiModelProperty(value = "生产商", example = "生产商")
        private String producer;
    }
} 