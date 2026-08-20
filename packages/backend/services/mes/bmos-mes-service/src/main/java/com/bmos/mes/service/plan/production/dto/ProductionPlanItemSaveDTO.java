package com.bmos.mes.service.plan.production.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.info.dto.ProductPlanRelationDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApiModel("生产计划item保存DTO")
@Data
public class ProductionPlanItemSaveDTO {

    @ApiModelProperty("分组信息")
    @NotNull
    private Integer groupNumber;

    @ApiModelProperty("计划开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startTime;

    @ApiModelProperty("计划结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate endTime;

    @ApiModelProperty("产线id")
    @NotNull
    private Long productionLineId;

    @ApiModelProperty("产线编码")
    @NotBlank
    private String productionLineCode;

    @ApiModelProperty("产线名称")
    @NotBlank
    private String productionLineName;

    @ApiModelProperty("计划编号")
    @NotBlank
    private String planNo;

    @ApiModelProperty("计划批号")
    @NotBlank
    private String batchNo;

    @ApiModelProperty("批号编号规则code")
    private String batchNoCode;

    @ApiModelProperty("计划编号规则code")
    private String planNoCode;

    @ApiModelProperty("生产批量")
    @NotNull
    private BigDecimal batchQuantity;

    @ApiModelProperty("生产计划模板详情id")
    @NotNull
    private Long templateBatchId;

    @ApiModelProperty("工序执行相关信息")
    @NotEmpty
    private List<ProcedureDetailDTO> procedureListDetail;

    @ApiModelProperty("确认指令单编号回传时间")
    private LocalDate planNoCodeApplyTime;

    @ApiModelProperty("当前关联生产批次下标列表")
    private List<ProductPlanRelationDTO> currentRelationList = new ArrayList<>();

    @ApiModelProperty("关联生产批次列表")
    private List<ProductPlanRelationDTO> relationList = new ArrayList<>();

    @ApiModelProperty("序号")
    @NotNull
    private Integer sort;

    @ApiModelProperty("前端回显使用关联批次")
    private String relatedBatchInfo;

    /**
     * 注意此处指令单类型值非枚举value
     * 而是用于编号生成的A、B、C
     */
    @ApiModelProperty("指令单类型")
    private String productPlanType;

    public void validProperties() {
        if (StrUtil.hasEmpty(planNo, batchNo)) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_ISSUE_BATCH_PARAM_ERROR);
        }
        if (productionLineId == null) {
            throw new BmosException(MesResponseCode.PLAN_ITEM_NOT_BIND_PRODUCTION_LINE, planNo);
        }
        if (groupNumber == null || startTime == null || endTime == null
                 || templateBatchId == null || sort == null || StrUtil.hasBlank(productionLineCode, productionLineName)) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_ISSUE_PARAM_ERROR);
        }
        if (CollUtil.isEmpty(procedureListDetail)) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_RELATION_ERROR);
        }
    }
}
