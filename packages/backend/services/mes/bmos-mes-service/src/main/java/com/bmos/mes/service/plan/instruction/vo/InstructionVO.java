package com.bmos.mes.service.plan.instruction.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.plan.InstructionStatusEnum;
import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailItemVO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
* 生产计划指令单表
*/
@Getter
@Setter
@ApiModel("InstructionVO:指令单详情VO")
public class InstructionVO {
    @ApiModelProperty("指令单id")
    private Long id;
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("生产工序节点id")
    private String nodeId;

    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureId;

    @ApiModelProperty("生产工序id")
    private Long procedureModelId;

    @ApiModelProperty("生产工序名称")
    private String procedureModelName;

    @ApiModelProperty("生产工序阶段编码")
    private String procedureModelCode;

    @ApiModelProperty("负责人角色")
    private Long principal;

    @ApiModelProperty("确认人id")
    private String confirmUserId;

    @ApiModelProperty("确认人名称")
    private String confirmUserName;

    @ApiModelProperty("指令单状态 已分解 RESOLVE已确认 CONFIRM")
    private InstructionStatusEnum status;

    @ApiModelProperty("班组列表")
    private List<InstructionTeamDetailItemVO> teams;

    @ApiModelProperty("排序")
    private Integer sort;

    public String getConfirmUserName(){
        BaseUserDO user = UserUtils.getUser(confirmUserId);
        return ObjectUtil.isNotNull(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
