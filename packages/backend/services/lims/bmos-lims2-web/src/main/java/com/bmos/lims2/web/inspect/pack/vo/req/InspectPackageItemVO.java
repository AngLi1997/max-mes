package com.bmos.lims2.web.inspect.pack.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 实验包下检验项目
 */
@Getter
@Setter
@ApiModel("实验包下检验项目VO")
public class InspectPackageItemVO {

    @ApiModelProperty(value = "检验项id", required = true)
    @NotNull
    private Long inspectItemId;

    @ApiModelProperty(value = "检验项目编码")
    private String  code;

    @ApiModelProperty(value = "检验项目名称")
    private String name;

}
