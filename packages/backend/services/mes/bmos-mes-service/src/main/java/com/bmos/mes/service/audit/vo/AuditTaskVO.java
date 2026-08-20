package com.bmos.mes.service.audit.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.common.enums.audit.FlowStateEnum;
import com.bmos.mes.service.utils.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "单个任务vo")
public class AuditTaskVO {

    @ApiModelProperty("审核人")
    private String assignee;

    @ApiModelProperty("审核人名称")
    private String assigneeName;

    @ApiModelProperty("审核时间")
    private LocalDateTime endTime;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("审核状态")
    private Integer state;

    @ApiModelProperty("状态")
    private String stateName;

    @ApiModelProperty("节点名称")
    private String elementName;

    @ApiModelProperty("节点key")
    private String elementKey;

    @ApiModelProperty("审核意见")
    private String comment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("抄送人员")
    private List<String> copyToUserName;

    @ApiModelProperty("审批原因")
    private String deleteReason;

    public String getAssigneeName(){
        return UserUtils.getUsername(StrUtil.subBefore(assignee,StrUtil.C_COLON,false));
    }

    public String getStateName(){
            if (StrUtil.equals(deleteReason, FlowStateEnum.APPROVE_REJECT.getCode())) {
                this.state = 5;
            }
            if (StrUtil.equals(deleteReason, FlowStateEnum.BACK_TO_PREV.getCode())) {
                this.state = 2;
            }
            return EnumUtils.getValueByName(FlowStateEnum.values(), String.valueOf(state));
    }
}
