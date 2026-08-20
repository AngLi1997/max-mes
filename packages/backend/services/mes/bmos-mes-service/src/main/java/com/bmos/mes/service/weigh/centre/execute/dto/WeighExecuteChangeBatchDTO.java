package com.bmos.mes.service.weigh.centre.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 物料称量切换批次dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("物料称量切换批次dto")
@Data
public class WeighExecuteChangeBatchDTO {

    /**
     * 需求id
     */
    @ApiModelProperty(value = "需求id", example = "1", required = true)
    @NotNull
    private Long requirementId;

    /**
     * 切换人id
     */
    @ApiModelProperty(value = "切换人id", example = "1")
    @NotNull
    private String changerId;
}
