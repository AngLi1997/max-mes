package com.bmos.mes.service.audit.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.audit.FlowStateEnum;
import com.bmos.mes.service.utils.UserUtils;
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
@ApiModel(value = "流程追溯vo")
public class AuditHistoryVO {

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("发起时间")
    private LocalDateTime processStartTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("发起人")
    private String startBy;

    @ApiModelProperty("发起人名称")
    private String startByName;

    @ApiModelProperty("结束状态")
    private String processState;

    @ApiModelProperty("结束状态")
    private FlowStateEnum processStateEnum;

    @ApiModelProperty("业务id")
    private String businessKey;

    @ApiModelProperty("业务code")
    private String extField;

    @ApiModelProperty(value = "业务名称")
    private String name;

    @ApiModelProperty(value = "流程名称")
    private String flowName;

    public FlowStateEnum getProcessStateEnum() {
        return FlowStateEnum.getEnumByState(processState);
    }

    public String getStartByName() {
        BaseUserDO user = UserUtils.getUser(startBy);
        if (ObjectUtil.isEmpty(user)) {
            return null;
        }
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }


}
