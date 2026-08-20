package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按检验项目查询检验录入记录-请求DTO（不分页）
 * @Author: yigaohui
 * @Date: 2025/09/11 11:15
 */
@Getter
@Setter
@ApiModel("按检验项目查询检验录入记录-请求DTO（不分页）")
public class EntryByItemQueryDTO {

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;
}


