package com.bmos.lims2.web.inspect.query.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 按检验项目分页查询检验录入记录-请求VO
 * @Author: yigaohui
 * @Date: 2025/09/11 10:40
 */
@Getter
@Setter
@ApiModel("按检验项目分页查询检验录入记录-请求VO")
public class EntryByItemPageQueryVO {

    @ApiModelProperty("检验单ID")
    @NotNull(message = "检验单ID不能为空")
    private Long inspectionOrderId;

    @ApiModelProperty("检验项目ID")
    @NotNull(message = "检验项目ID不能为空")
    private Long inspectItemId;

    @ApiModelProperty("页码，从1开始")
    private Integer pageNum = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 20;
}


