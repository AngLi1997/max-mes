package com.bmos.lims2.web.inspect.retention.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 留样观察提交请求VO
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
@ApiModel("留样观察提交请求")
public class RetentionObservationSubmitReqVO {

    @ApiModelProperty(value = "观察结果（true-符合，false-不符合）", required = true)
    @NotNull(message = "观察结果不能为空")
    private Boolean observationResult;

    @ApiModelProperty("观察备注")
    private String observationRemark;
}
