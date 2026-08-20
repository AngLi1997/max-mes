package com.bmos.lims2.server.audit.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.FlowStateEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@ApiModel(value = "查询任务历史")
public class TaskHistoryVO {

    @ApiModelProperty("节点名称")
    private String elementName;

    @ApiModelProperty("节点标识")
    private String elementKey;

    @ApiModelProperty("处理人")
    private String assignee;

    @ApiModelProperty("处理人名称")
    private String assigneeName;

    @ApiModelProperty("实际处理人")
    private String completeBy;

    @ApiModelProperty("实际处理人名称")
    private String completeName;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("处理状态")
    private FlowStateEnum stateName;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("审批意见")
    private String comment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("审批不通过异常原因")
    private String deleteReason;

    public FlowStateEnum getStateName() {
        if (StrUtil.equals(deleteReason, FlowStateEnum.APPROVE_REJECT.getCode())) {
            this.state = 5;
        }
        if (StrUtil.equals(deleteReason, FlowStateEnum.BACK_TO_PREV.getCode())) {
            this.state = 2;
        }
        return FlowStateEnum.getEnumByState(String.valueOf(state));
    }

    public String getAssigneeName() {
        return UserUtils.getUsername(StrUtil.subBefore(assignee, StrUtil.C_COLON, false));
    }

    public String getCompleteName() {
        BaseUserDO user = UserUtils.getUser(completeBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
