package com.bmos.lims2.web.inspect.sample.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("处理条目（按样品提交）")
public class SampleProcessItemReqVO {

    @ApiModelProperty("样品ID")
    @NotNull
    private Long sampleId;

    @ApiModelProperty("处理方式")
    @NotNull
    private String processMethod;

    @ApiModelProperty("处理备注")
    private String processRemark;
}



