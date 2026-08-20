package com.bmos.lims2.server.inspect.scheme.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
@ApiModel("记录项组件配置VO")
public class ComponentConfigDTO {

    /**
     * 配置信息JSON
     */
    @ApiModelProperty("配置信息JSON")
    @NotBlank
    private String configInfo;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    /**
     * field_id
     */
    @ApiModelProperty("组件前端标识id")
    @NotNull
    private Long fieldId;
}
