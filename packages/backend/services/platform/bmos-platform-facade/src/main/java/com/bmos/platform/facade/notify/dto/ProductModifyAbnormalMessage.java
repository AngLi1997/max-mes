package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 生产修订
 *
 * @className: ProductModifyAbnormalMessageContext
 * @author: yigaohui
 * @date: 2025/1/8 15:14
 * @Version: 1.0
 * @description:
 */

@Data
public class ProductModifyAbnormalMessage {
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
