package com.bmos.lims2.server.audit.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.enums.EnumUtils;
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
@ApiModel(value = "节点信息vo")
public class FlowAuditNodeVO {

    @ApiModelProperty(value = "节点名称")
    private String elementName;

    @ApiModelProperty(value = "节点id")
    private String elementKey;

    @ApiModelProperty(value = "处理人")
    private String assignee;

    @ApiModelProperty(value = "实际处理人")
    private String completeBy;

    @ApiModelProperty(value = "处理人名称")
    private String assigneeName;

    @ApiModelProperty(value = "处理行为")
    private Integer state;

    @ApiModelProperty(value = "处理行为名称")
    private String stateName;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "审批意见")
    private String comment;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getStateName() {
        return EnumUtils.getNameByValue(FlowStateEnum.values(),String.valueOf(state));
    }

    public String getAssigneeName(){
        BaseUserDO user = UserUtils.getUser(completeBy);
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName()+StrUtil.DASHED+user.getLoginName()) : "";
    }
}
