package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 发起请验DTO
 */
@Getter
@Setter
@ApiModel(value = "发起请验DTO")
public class InitiateInspectDTO {

    /**
     * 计划id
     */
    @ApiModelProperty(value = "计划id", required = true)
    @NotNull
    private Long planId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "所需要请验的物料批号", required = true)
    @NotNull
    private String materialBatchNo;

    /**
     * 请验单号
     */
    @ApiModelProperty(value = "请验单号（三方/本地路径由前端传入；自研LIMS路径由LIMS生成，可不传）")
    private String inspectNo;

    /**
     * 配方物料id
     */
    @ApiModelProperty(value = "配方物料id", required = true)
    @NotNull
    private Long formulaMaterialId;

    /**
     * 工序模型id
     */
    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    /**
     * 工步模型id
     */
    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    /**
     * 工艺换班次数
     */
    @ApiModelProperty("工艺换班次数")
    private Long processChangeNumber;

    /**
     * 工序换班次数
     */
    @ApiModelProperty("工序换班次数")
    private Long procedureChangeNumber;

    /**
     * 请验配置数据id
     */
    @ApiModelProperty(value = "请验配置数据id", required = true)
    @NotNull
    private Long inspectConfigId;

    /**
     * 请验单信息
     */
    @ApiModelProperty(value = "请验单信息", required = true)
    @NotNull
    private List<InitiateInspectInfoDTO> initiateInspectInfoDTOList;

    /**
     * 检验方案id（自研LIMS路径）
     */
    @ApiModelProperty("检验方案id（自研LIMS路径）")
    private Long schemeId;

    /**
     * 检验方案版本id（自研LIMS路径）
     */
    @ApiModelProperty("检验方案版本id（自研LIMS路径）")
    private Long schemeVersionId;

}
