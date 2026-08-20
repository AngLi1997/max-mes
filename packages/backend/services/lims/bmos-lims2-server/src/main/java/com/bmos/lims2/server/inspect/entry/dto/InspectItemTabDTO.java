package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 检验信息-检验项目分页签
 * @Author: yigaohui
 * @Date: 2025/09/11 10:30
 */
@Getter
@Setter
@ApiModel("检验信息-检验项目分页签")
public class InspectItemTabDTO {

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;
}


