package com.bmos.lims2.web.inspect.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 时长预览响应
 * @Author: yigaohui
 * @Date: 2025/10/31 10:00
 */
@Data
@ApiModel("时长预览响应")
public class TimeDurationPreviewRespVO {

    @ApiModelProperty("格式化后的时长显示")
    private String calculateResult;

    @ApiModelProperty("按舍入规则换算后的秒值")
    private String timeSeconds;
}


