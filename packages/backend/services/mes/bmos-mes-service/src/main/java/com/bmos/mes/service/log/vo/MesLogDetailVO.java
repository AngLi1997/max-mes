package com.bmos.mes.service.log.vo;

import com.bmos.logging.util.LogTranslateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Mes操作日志详情VO")
public class MesLogDetailVO {

    @ApiModelProperty("操作对象")
    private String operationObject;

    @ApiModelProperty("操作详情")
    private String operationDetail;

    public String getOperationDetail() {
        return LogTranslateUtil.translateJson(operationObject);
    }

}
