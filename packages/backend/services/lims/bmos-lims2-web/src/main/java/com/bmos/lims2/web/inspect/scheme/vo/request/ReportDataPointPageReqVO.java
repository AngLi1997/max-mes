package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("报告数据点分页查询请求")
public class ReportDataPointPageReqVO {

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("检验项目ID（可选）")
    private Long inspectItemId;

    @ApiModelProperty("分析项ID（可选）")
    private Long parameterId;

    @ApiModelProperty("页码")
    private Integer pageNum = 1;

    @ApiModelProperty("每页条数")
    private Integer pageSize = 10;
}


