package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工艺关联详情VO")
public class ProcessRelationDetailVO {

    @ApiModelProperty("关联工艺id")
    private Long processId;

    @ApiModelProperty("关联工艺名称")
    private String processName;

}
