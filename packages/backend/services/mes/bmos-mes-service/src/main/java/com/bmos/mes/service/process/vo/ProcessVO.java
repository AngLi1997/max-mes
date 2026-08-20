package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("工艺VO")
public class ProcessVO {

    @ApiModelProperty("版本id")
    private Long id;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("流程模型id")
    private String processModelId;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty("启用状态")
    private Boolean state;
}
