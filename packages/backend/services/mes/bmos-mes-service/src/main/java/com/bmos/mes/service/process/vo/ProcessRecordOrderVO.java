package com.bmos.mes.service.process.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel("工艺记录顺序VO")
public class ProcessRecordOrderVO {

    @ApiModelProperty("步骤模型id")
    private Long id;

    @ApiModelProperty("步骤id")
    private Long procedureStepId;

    @ApiModelProperty("记录项名称")
    private String recordItemName;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    private Long recordVersionId;

    @ApiModelProperty("工序，工序步骤名称")
    private String procedureName;

    @ApiModelProperty("排序")
    private Long recordItemOrder;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private LocalDateTime createTime;

    @ApiModelProperty("是否可复用")
    private Boolean reusable;
}
