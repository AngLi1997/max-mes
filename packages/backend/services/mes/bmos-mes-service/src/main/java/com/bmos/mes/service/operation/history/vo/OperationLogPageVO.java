package com.bmos.mes.service.operation.history.vo;

import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.service.operation.history.enums.OperationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel("操作日志VO")
public class OperationLogPageVO {

    @ApiModelProperty("操作类型")
    private String operationType;

    @ApiModelProperty("操作类型名称")
    private String operationTypeName;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("操作人")
    private String createBy;

    @ApiModelProperty("操作人名称")
    private String createUsername;


    public String getOperationTypeName() {
        return EnumUtils.getNameByValue(OperationType.values(), operationType);
    }
}
