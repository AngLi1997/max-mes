package com.bmos.mes.service.log.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.mes.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel("操作日志详情")
@Data
public class OperationLogDetailDTO {

    @NotNull
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("所要查看详情的那一条数据的所对应的操作时间")
    @NotEmpty
    private String operationTime;

    public LocalDateTime getOperationTime() {
        return LocalDateTimeUtil.parse(operationTime, TimeUtil.F_DATETIME);
    }

}
