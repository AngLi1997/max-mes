package com.bmos.lims2.server.inspect.scheme.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("工序步骤组件配置集合查询")
public class SchemeParameterComponentConfigListQueryDTO {

    @ApiModelProperty("方案id")
    @NotNull
    private Long schemeId;

    @ApiModelProperty("方案版本id")
    @NotNull
    private Long schemeVersionId;

    @ApiModelProperty("分析项id")
    @NotNull
    private Long parameterId;

    @ApiModelProperty("分析项配置id")
    private Long parameterConfigId;
}
