package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @className: MaterialForeWarningMessageVO
 * @author: yigaohui
 * @date: 2025/1/8 14:00
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("物料近效期预警消息")
public class MaterialForeWarningMessage {

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("时间")
    private LocalDateTime time;
}
