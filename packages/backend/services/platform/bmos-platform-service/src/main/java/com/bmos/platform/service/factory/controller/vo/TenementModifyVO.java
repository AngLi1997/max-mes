package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @className: TenementAddVO
 * @author: yigaohui
 * @date: 2024/12/30 13:37
 * @Version: 1.0
 * @description:
 */
@Data
@ApiModel("楼宇修改VO")
public class TenementModifyVO extends TenementAddVO {
    @ApiModelProperty("楼宇id")
    @NotNull
    private Long id;
}
