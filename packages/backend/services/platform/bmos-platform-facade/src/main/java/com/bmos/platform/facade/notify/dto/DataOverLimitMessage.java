package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("数据超限异常")
public class DataOverLimitMessage {

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("步骤名称")
    private String procedureStepName;

    @ApiModelProperty("异常描述")
    private String abnormalDescription;

    @ApiModelProperty("时间")
    private LocalDateTime time;

}
