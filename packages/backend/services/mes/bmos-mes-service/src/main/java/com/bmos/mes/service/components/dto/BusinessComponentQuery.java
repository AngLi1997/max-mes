package com.bmos.mes.service.components.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/31 10:06
 */
@Data
@ApiModel("业务组件实例查询参数")
public class BusinessComponentQuery {

    @ApiModelProperty(value = "生产计划id", required = true, example = "1")
    private Long productPlanId;

    @ApiModelProperty(value = "工序步骤模型id", required = true, example = "1")
    private Long procedureStepModelId;

    @ApiModelProperty(value = "业务组件id", required = true, example = "1")
    private Long componentId;

    @ApiModelProperty(value = "拷贝版本", required = true, example = "1")
    private Long copyVersion;

    @ApiModelProperty(value = "是否复用", required = true, example = "true")
    private Boolean reuse;
}
