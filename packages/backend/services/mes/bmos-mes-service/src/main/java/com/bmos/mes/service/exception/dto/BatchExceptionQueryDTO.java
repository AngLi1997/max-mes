package com.bmos.mes.service.exception.dto;

import com.bmos.mybatis.page.BasePage;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("批次异常信息分页VO")
public class BatchExceptionQueryDTO extends BasePage {

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelEnumProperty(value = "异常状态", enumClass = com.bmos.mes.common.enums.execute.ExceptionStatusEnum.class)
    private String exceptionStatus;

    @ApiModelProperty("异常类型")
    private String exceptionType;

    @ApiModelProperty("异常描述")
    private String exceptionDescription;

    @ApiModelProperty("批次追溯查询")
    private boolean traceQuery;

}
