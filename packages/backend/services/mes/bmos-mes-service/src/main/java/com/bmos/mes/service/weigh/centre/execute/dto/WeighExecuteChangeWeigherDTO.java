package com.bmos.mes.service.weigh.centre.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 物料称量切换称量人dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("物料称量切换称量人dto")
@Data
public class WeighExecuteChangeWeigherDTO {

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id", example = "1", required = true)
    @NotNull
    private Long taskId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1", required = true)
    @NotBlank
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "1")
    private String remark;
}
