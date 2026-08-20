package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 配液投入容器扫描DTO
 */
@Getter
@Setter
@ApiModel("配液投入容器扫描DTO")
public class ScanPreparationInputDTO {

    /**
     * 组件id
     */
    @ApiModelProperty("配料投入组件实例id")
    @NotNull
    private Long componentInstanceId;

    /**
     * 工艺id
     */
    @ApiModelProperty("工艺id")
    private Long processId;

    /**
     * 工艺版本
     */
    @ApiModelProperty("工艺版本")
    private String processVersion;
}
