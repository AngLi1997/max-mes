package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 批量录入判定结论DTO
 */
@Getter
@Setter
@ApiModel("批量录入判定结论DTO")
public class BatchJudgmentDTO {

    @ApiModelProperty("任务ID")
    @NotNull(message = "任务ID不能为空")
    private Long id;

    @ApiModelProperty("判定结果：true-通过，false-不通过")
    @NotNull(message = "判定结果不能为空")
    private Boolean judgedResult;

    @ApiModelProperty("判定时间（可空，不传则使用当前时间）")
    private LocalDateTime judgedTime;
}


