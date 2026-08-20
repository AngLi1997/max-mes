package com.bmos.lims2.web.inspect.query.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 方案参数（检验项目与分析项）下拉项
 * @Author: yigaohui
 * @Date: 2025/09/05 12:28
 */
@Getter
@Setter
@ApiModel("方案参数下拉项")
public class SchemeParameterOptionRespVO {
    @ApiModelProperty("方案项目ID")
    private Long schemeItemId;
    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;
    @ApiModelProperty("检验项目名称")
    private String inspectItemName;
    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;
    @ApiModelProperty("方案分析项ID")
    private Long schemeParameterId;
    @ApiModelProperty("分析项ID")
    private Long parameterId;
    @ApiModelProperty("分析项名称")
    private String parameterName;
    @ApiModelProperty("分析项编码")
    private String parameterCode;
}


