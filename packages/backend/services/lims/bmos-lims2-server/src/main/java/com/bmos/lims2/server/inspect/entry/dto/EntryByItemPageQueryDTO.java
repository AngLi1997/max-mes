package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按检验项目分页查询检验录入记录-请求DTO
 * @Author: yigaohui
 * @Date: 2025/09/11 10:35
 */
@Getter
@Setter
@ApiModel("按检验项目分页查询检验录入记录-请求DTO")
public class EntryByItemPageQueryDTO {

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("页码，从1开始")
    private Integer pageNum = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 20;
}


