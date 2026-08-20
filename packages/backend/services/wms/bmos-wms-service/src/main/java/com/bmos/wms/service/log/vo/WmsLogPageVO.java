package com.bmos.wms.service.log.vo;

import com.bmos.logging.enums.OperationTypeEnum;
import com.bmos.logging.util.LogTranslateUtil;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.wms.service.utils.UserUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import serializer.I18nSerializer;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ApiModel("Wms操作日志VO")
public class WmsLogPageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("操作类型")
    private OperationTypeEnum operationType;

    @ApiModelProperty("业务操作")
    @JsonSerialize(using = I18nSerializer.class)
    private String operationBusiness;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("操作人")
    private String userName;

    @ApiModelProperty("操作人loginName")
    private String loginName;

    @ApiModelProperty("操作人id")
    private String userId;

    @ApiModelProperty("操作对象")
    private String operationObject;


    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty("操作详情")
    private String operationDetail;

    public String getOperationDetail() {
        return LogTranslateUtil.translateJson(operationObject);
    }

    public String getLoginName(){
        return Optional.ofNullable(UserUtils.getUser(userId)).orElse(new BaseUserDO()).getLoginName();
    }
}
