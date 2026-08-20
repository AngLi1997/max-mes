package com.bmos.mes.service.execute.vo;

import com.bmos.mes.service.utils.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel("填报数据VO")
public class FormDataVO {

    @ApiModelProperty("组件id")
    private Long fieldId;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值扩展")
    private String valueExtension;

    @ApiModelProperty("操作类型")
    private String operationType;

    @ApiModelProperty("操作类型名称")
    private String operationTypeName;

    @ApiModelProperty("操作人id")
    private String operationUser;

    @ApiModelProperty("操作人名称")
    private String operationUsername;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("复核人")
    private String reviewUser;

    @ApiModelProperty("复核人名称")
    private String reviewUsername;

    @ApiModelProperty("复核时间")
    private LocalDateTime reviewTime;

    @ApiModelProperty("是否是系统计算")
    private Boolean systemCreate;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否是空值")
    private Boolean emptyValue;

    public String getOperationUsername() {
        return UserUtils.getUsername(operationUser);
    }

    public String getReviewUsername() {
        return UserUtils.getUsername(reviewUser);
    }
}
