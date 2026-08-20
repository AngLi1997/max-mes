package com.bmos.platform.service.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: MaterialForeWarningMessageVO
 * @author: yigaohui
 * @date: 2025/1/8 14:00
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("物料近效期预警消息上下文")
public class MaterialForeWarningMessageContext extends MessageContextDTO {

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialCode;
}
