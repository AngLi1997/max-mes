package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("请验结果组件确认回填表单数据DTO")
@Data
public class InspectComponentConfirmFillDTO {

    @ApiModelProperty("顶层组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("批次分组组件id")
    @NotNull
    private Long groupComponentId;

    @ApiModelProperty("请验单id")
    @NotNull
    private Long inspectId;

    @ApiModelProperty("请验结果组件实例id")
    @NotNull
    private Long instanceId;

}
