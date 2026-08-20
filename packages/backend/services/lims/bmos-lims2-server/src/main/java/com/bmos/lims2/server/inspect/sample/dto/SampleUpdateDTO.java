package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @className: SamplingConfirmDTO
 * @author: yigaohui
 * @date: 2025/8/18 11:49
 * @Version: 1.0
 * @description:
 */

@Data
public class SampleUpdateDTO {

    @ApiModelProperty(value = "请验单ID", required = true)
    private Long inspectionOrderId;

    @ApiModelProperty(value = "样品列表", required = true)
    @NotEmpty(message = "样品列表不能为空")
    private List<SampleDTO> samples;

    @ApiModelProperty(value = "取样人", required = true)
    @NotBlank(message = "取样人不能为空")
    private String samplerName;

    @ApiModelProperty(value = "取样人ID",required = true)
    @NotNull(message = "取样人ID不能为空")
    private Long samplerId;

    @ApiModelProperty("备注")
    private String remark;
}
