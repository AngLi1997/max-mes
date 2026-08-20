package com.bmos.lims2.web.inspect.query.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 按检验项目查询检验录入记录-请求VO（不分页）
 * @Author: yigaohui
 * @Date: 2025/09/11 11:16
 */
@Getter
@Setter
@ApiModel("按检验项目查询检验录入记录-请求VO（不分页）")
public class EntryByItemQueryVO {

    @ApiModelProperty("检验单ID")
    @NotNull(message = "检验单ID不能为空")
    private Long inspectionOrderId;

    @ApiModelProperty("检验项目ID")
    @NotNull(message = "检验项目ID不能为空")
    private Long inspectItemId;
}


