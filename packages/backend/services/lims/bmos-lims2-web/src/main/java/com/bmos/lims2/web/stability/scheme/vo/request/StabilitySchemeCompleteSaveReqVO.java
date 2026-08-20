package com.bmos.lims2.web.stability.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性方案版本完成请求VO（融合基础信息 + 检验项目配置 + 检验计划，后台校验必填项并将版本置为已完成）
 */
@Data
@ApiModel("稳定性方案版本完成请求")
public class StabilitySchemeCompleteSaveReqVO {

    @ApiModelProperty(value = "基础信息", required = true)
    @NotNull(message = "基础信息不能为空")
    @Valid
    private StabilitySchemeBasicSaveReqVO basic;

    @ApiModelProperty("检验项目配置列表")
    @Valid
    private List<StabilitySchemeItemSaveReqVO.ItemVO> itemUpdates;

    @ApiModelProperty("检验计划列表")
    @Valid
    private List<StabilitySchemePlanSaveReqVO.PlanVO> plans;
}
