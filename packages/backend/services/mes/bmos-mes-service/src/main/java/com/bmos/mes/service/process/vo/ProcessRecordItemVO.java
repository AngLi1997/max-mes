package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel("工艺步骤记录项vo")
public class ProcessRecordItemVO {

    @ApiModelProperty(value = "批记录版本id")
    private Long recordVersionId;

    @ApiModelProperty(value = "记录项id")
    private Long recordItemId;

    @ApiModelProperty(value = "工序模型id")
    private Long procedureModelId;

    @ApiModelProperty(value = "记录类型")
    private String nodeFunction;

    @ApiModelProperty(value = "工步/任务名称")
    private String name;

    @ApiModelProperty(value = "工序名称")
    private String procedureName;
}
