package com.bmos.platform.service.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: DataOutLimitMessageVO
 * @author: yigaohui
 * @date: 2025/1/8 13:59
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("数据超限消息上下文")
public class DataOutLimitMessageContext extends MessageContextDTO {

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
}
