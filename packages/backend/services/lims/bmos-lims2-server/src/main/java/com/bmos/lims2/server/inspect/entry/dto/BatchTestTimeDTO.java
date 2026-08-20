package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量设置检验时间DTO
 *
 * @author system
 * @since 2025/01/30
 */
@Getter
@Setter
@ApiModel("批量设置检验时间数据对象")
public class BatchTestTimeDTO {

    @ApiModelProperty("任务ID")
    @NotNull(message = "任务ID不能为空")
    private Long id;

    @ApiModelProperty("检验时间")
    @NotNull(message = "检验时间不能为空")
    private LocalDateTime testTime;
}
