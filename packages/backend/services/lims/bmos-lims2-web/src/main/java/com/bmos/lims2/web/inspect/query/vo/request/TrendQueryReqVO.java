package com.bmos.lims2.web.inspect.query.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("趋势查询请求VO")
public class TrendQueryReqVO {
    @ApiModelProperty(value = "检品ID", required = true)
    @NotNull
    private Long materialId;
    @ApiModelProperty(value = "方案ID", required = true)
    @NotNull
    private Long schemeId;
    @ApiModelProperty(value = "检验项目ID", required = true)
    @NotNull
    private Long inspectItemId;
    @ApiModelProperty(value = "分析项ID", required = true)
    @NotNull
    private Long parameterId;
    @ApiModelProperty(value = "数据点名称", required = true)
    @NotNull
    private String dataPointName;
    @ApiModelProperty("请验开始时间")
    private java.time.LocalDateTime requestStartTime;
    @ApiModelProperty("请验结束时间")
    private java.time.LocalDateTime requestEndTime;
}


