package com.bmos.mes.service.execute.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("生产计划组件修订记录VO")
@Data
public class PlanFieldModifyVO {

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工序步骤名称")
    private String procedureStepName;

    @ApiModelProperty("fieldId")
    private Long fieldId;

    @ApiModelProperty("原值")
    private String originalValue;

    @ApiModelProperty("新值")
    private String newValue;

    @ApiModelProperty("修订人id")
    private String operationUser;

    @ApiModelProperty("修订人名称")
    private String operationUserName;

    @ApiModelProperty("复核人id")
    private String reviewUser;

    @ApiModelProperty("复核人名称")
    private String reviewUserName;

    @ApiModelProperty("修订时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("备注")
    private String remark;

    public String getOperationUserName() {
        BaseUserDO user = UserUtils.getUser(operationUser);
        return user.getLoginName() + StrUtil.DASHED + user.getUserName();
    }

    public String getReviewUserName() {
        if (StrUtil.isEmpty(reviewUser)) {
            return StrUtil.EMPTY;
        }
        BaseUserDO user = UserUtils.getUser(reviewUser);
        return user.getLoginName() + StrUtil.DASHED + user.getUserName();
    }

}
