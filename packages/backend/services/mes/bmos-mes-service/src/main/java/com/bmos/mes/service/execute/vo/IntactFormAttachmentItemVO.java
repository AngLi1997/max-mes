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
public class IntactFormAttachmentItemVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("路径")
    private String path;

}
