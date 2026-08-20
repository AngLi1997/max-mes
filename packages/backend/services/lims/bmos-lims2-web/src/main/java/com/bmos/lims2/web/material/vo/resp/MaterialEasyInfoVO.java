package com.bmos.lims2.web.material.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检品简单信息VO")
public class MaterialEasyInfoVO {

    @ApiModelProperty("检品当前系统id")
    private String id;

    @ApiModelProperty("检品名称")
    private String name;

    @ApiModelProperty("检品编码")
    private String mergeCode;

    @ApiModelProperty("规格")
    private String specification;

}
