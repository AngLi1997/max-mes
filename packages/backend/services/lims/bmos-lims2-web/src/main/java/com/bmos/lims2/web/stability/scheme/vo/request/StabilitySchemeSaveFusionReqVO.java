package com.bmos.lims2.web.stability.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案全量暂存请求VO（不校验必填字段）
 */
@Data
@ApiModel("稳定性方案全量暂存请求（不校验必填）")
public class StabilitySchemeSaveFusionReqVO {

    @ApiModelProperty("基础信息（暂存不校验必填）")
    private StabilitySchemeBasicSaveReqVO basic;

    @ApiModelProperty("检验项目配置更新列表（等价于 /stability-scheme/update-items 的入参，嵌套结构：检验项目→分析项列表）")
    private List<StabilitySchemeItemSaveReqVO.ItemVO> itemUpdates;

    @ApiModelProperty("检验计划列表")
    private List<StabilitySchemePlanSaveReqVO.PlanVO> plans;
}
