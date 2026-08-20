package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("工艺分页VO")
public class ProcessPageVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("规格")
    private String specification;


    @ApiModelProperty("启用版本")
    private String activeVersion;

    @ApiModelProperty("产品合并编码")
    private String mergeCode;

    @ApiModelProperty("看板配置版本")
    private String dashboardConfigVersion;
}
