package com.bmos.mes.service.exception.dto;

import com.bmos.mes.common.enums.execute.ModifyExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("记录修订异常DTO")
@Getter
@Setter
@Builder
public class RecordModifyExceptionDTO {

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("修订操作类型")
    private ModifyExceptionEnum modifyException;

    @ApiModelProperty("数据列表")
    private List<RecordModifyItemDTO> itemList;

}
