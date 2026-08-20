package com.bmos.lims2.server.inspect.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按方案ID查询检验单下拉-请求DTO
 * @Author: yigaohui
 * @Date: 2025/09/08 10:20
 */
@Getter
@Setter
@ApiModel("按方案ID查询检验单选项查询条件")
public class InspectionOrderBySchemeQueryDTO {

    @ApiModelProperty(value = "方案ID", required = true)
    private Long schemeId;

    @ApiModelProperty(value = "检验单状态，可选")
    private String orderStatus;
}


