package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @className: TenementFloorAddVO
 * @author: yigaohui
 * @date: 2024/12/30 14:32
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("楼层修改VO")
public class TenementFloorModifyVO extends TenementFloorAddVO {

    @ApiModelProperty("id")
    @NotNull
    private Long id;
}
