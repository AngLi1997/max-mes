package com.bmos.mes.service.weigh.centre.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 物料称量称量签名DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 17:20
 */
@Data
@ApiModel("物料称量称量签名DTO")
public class WeighExecuteWeighSignDTO {

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id", example = "1", required = true)
    @NotNull
    private Long taskId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    @NotNull
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    @NotNull
    private String reCheckerId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;
}
