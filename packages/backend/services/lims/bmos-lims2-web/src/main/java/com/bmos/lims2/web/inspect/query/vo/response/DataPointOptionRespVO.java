package com.bmos.lims2.web.inspect.query.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 数据点下拉项
 * @Author: yigaohui
 * @Date: 2025/09/05 12:29
 */
@Getter
@Setter
@ApiModel("数据点下拉项")
public class DataPointOptionRespVO {
    @ApiModelProperty("方案数据点配置ID")
    private Long id;
    @ApiModelProperty("原始数据点ID")
    private Long dataPointId;
    @ApiModelProperty("数据点名称")
    private String name;
}


