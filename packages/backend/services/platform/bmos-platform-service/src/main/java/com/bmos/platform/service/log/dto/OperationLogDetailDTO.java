package com.bmos.platform.service.log.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.logging.util.LogTranslateUtil;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.service.log.vo.OperationLogPageVO;
import com.bmos.platform.service.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ApiModel("操作日志详情")
public class OperationLogDetailDTO{

    @NotNull
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("所选数据的operationTime")
    @NotNull
    private String operationTime;

    public LocalDateTime getOperationTime(){
        return LocalDateTimeUtil.parse(operationTime, GlobalConstants.DATE_TIME_FORMAT);
    }



}
