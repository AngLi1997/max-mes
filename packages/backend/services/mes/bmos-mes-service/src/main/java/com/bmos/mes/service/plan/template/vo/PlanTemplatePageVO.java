package com.bmos.mes.service.plan.template.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("生产计划模板分页VO")
@Data
public class PlanTemplatePageVO {

    @ApiModelProperty("生产计划模板id")
    private Long id;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("确认状态")
    private Boolean confirmed;

    @ApiModelProperty("启停状态")
    private Boolean state;

    @ApiModelProperty("操作人id")
    private String operatorUserId;

    @ApiModelProperty("操作人名称")
    private String operatorUserName;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    public String getOperatorUserName() {
        BaseUserDO user = UserUtils.getUser(operatorUserId);
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

}
