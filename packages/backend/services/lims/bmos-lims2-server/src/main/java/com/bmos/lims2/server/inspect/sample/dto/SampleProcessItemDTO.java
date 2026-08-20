package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 处理条目
 * @Author: yigaohui
 * @Date: 2025/09/10 10:30
 */
@Getter
@Setter
@ApiModel("处理条目")
public class SampleProcessItemDTO {

    @ApiModelProperty("样品ID")
    @NotNull
    private Long sampleId;

    @ApiModelProperty("处理方式")
    @NotNull
    private String processMethod;

    @ApiModelProperty("处理备注")
    private String processRemark;
}



