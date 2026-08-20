package com.bmos.lims2.web.inspect.retention.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 留样样品打印标签请求VO
 * @Author: yigaohui
 * @Date: 2026/02/12
 */
@Data
@ApiModel("留样样品打印标签请求")
public class RetentionSamplePrintTagReqVO {

    @ApiModelProperty(value = "样品编号", required = true)
    @NotBlank(message = "样品编号不能为空")
    private String sampleNo;
}
