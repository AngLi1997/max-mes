package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: FactoryLineDTO
 * @author: yigaohui
 * @date: 2025/2/5 14:03
 * @Version: 1.0
 * @description:
 */

@ApiModel("编码名称")
@Data
public class CodeNameVO {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;
}
