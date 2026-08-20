package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("查询完整数据itemVO")
public class IntactFormDataItemVO {

    @ApiModelProperty("字段id")
    private Long fieldId;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值扩展")
    private String valueExtension;

    @ApiModelProperty("组件类型")
    private String componentType;

}
