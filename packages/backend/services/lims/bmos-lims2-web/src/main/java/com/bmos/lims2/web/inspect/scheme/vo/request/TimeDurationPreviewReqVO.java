package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.bmos.lims2.common.enums.RoundingRuleEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 时长预览请求
 * @Author: yigaohui
 * @Date: 2025/10/31 10:00
 */
@Data
@ApiModel("时长预览请求")
public class TimeDurationPreviewReqVO {

    @ApiModelProperty(value = "开始时间，格式yyyy-MM-dd HH:mm:ss", required = true)
    @NotBlank
    private String startTime;

    @ApiModelProperty(value = "结束时间，格式yyyy-MM-dd HH:mm:ss", required = true)
    @NotBlank
    private String endTime;

    @ApiModelProperty(value = "显示格式（dd HH:mm:ss、HH:mm:ss、mm:ss、ss、dd HH:mm、dd HH、dd、HH:mm、HH、mm）")
    private String calculateType;

    @ApiModelProperty(value = "舍入规则：UP向上，DOWN向下")
    @NotNull
    private RoundingRuleEnum roundingUp;
}


