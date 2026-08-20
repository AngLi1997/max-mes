package com.bmos.mes.service.weigh.centre.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 物料称量完成称量DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 17:20
 */
@Data
@ApiModel("物料称量完成称量DTO")
public class WeighExecuteWeighFinishDTO {

    /**
     * 需求id
     */
    @ApiModelProperty(value = "需求id", example = "1", required = true)
    @NotNull
    private Long requirementId;

    /**
     * 完成人id
     */
    @ApiModelProperty(value = "完成人id", example = "1")
    @NotNull
    private String finisherId;
}
