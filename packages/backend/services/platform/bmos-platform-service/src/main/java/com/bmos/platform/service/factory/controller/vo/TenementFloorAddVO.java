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
@ApiModel("楼层添加VO")
public class TenementFloorAddVO {
    /**
     * 楼栋id
     */
    @ApiModelProperty("楼栋id")
    @NotNull
    private Long tenementId;
    /**
     * 编码
     */
    @ApiModelProperty("楼层编码")
    @NotNull
    private String code;
    /**
     * 楼层名称
     */
    @ApiModelProperty("楼层名称")
    @NotNull
    private String name;
}
