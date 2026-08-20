package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 恢复稳定性考察计划请求VO
 */
@Data
@ApiModel("恢复稳定性考察计划请求")
public class StabilityInspectPlanResumeReqVO {

    @ApiModelProperty("方案版本ID（可选，恢复时可切换为其他生效版本）")
    private Long schemeVersionId;
}
