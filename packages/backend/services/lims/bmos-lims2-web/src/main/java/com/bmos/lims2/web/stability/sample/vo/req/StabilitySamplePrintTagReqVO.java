package com.bmos.lims2.web.stability.sample.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 稳定性整体样品打印标签请求VO
 */
@Data
@ApiModel("稳定性整体样品打印标签请求")
public class StabilitySamplePrintTagReqVO {

    @ApiModelProperty(value = "样品编号", required = true)
    @NotBlank(message = "样品编号不能为空")
    private String sampleNo;
}
