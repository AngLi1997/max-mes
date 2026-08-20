package com.bmos.mes.service.lotrelease.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批签发生产计划vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 11:26
 */
@Data
@ApiModel("批签发生产计划vo")
public class LotReleasePlanVO {

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批号", example = "CPX001231102")
    private String batchNo;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    private String processName;

    @ApiModelProperty(value = "生产开始时间", example = "2024-08-20 11:26:00")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "生产结束时间", example = "2024-08-20 11:26:00")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "异常数量", example = "1")
    private Integer errorCount;
}
