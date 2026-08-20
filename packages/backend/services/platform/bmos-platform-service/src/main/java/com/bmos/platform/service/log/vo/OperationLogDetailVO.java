package com.bmos.platform.service.log.vo;

import com.bmos.logging.util.LogTranslateUtil;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.service.utils.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ApiModel("操作日志VO")
public class OperationLogDetailVO extends OperationLogPageVO {

    @ApiModelProperty("操作对象")
    private String operationObject;

    @ApiModelProperty("操作详情")
    private String operationDetail;

    public String getOperationDetail() {
        return LogTranslateUtil.translateJson(operationObject);
    }

    public String getLoginName(){
        return Optional.ofNullable(UserUtils.getUser(this.getUserId())).orElse(new BaseUserDO()).getLoginName();
    }
}
