package com.bmos.mes.service.workflow.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.process.ProcessStateEnum;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ProcedureProgressVO
 * @Description 工序生产进度vo
 * @Author Ren Jin Guang
 * @Date 2024/8/23 16:18
 */
@Setter
@Getter
@ToString
@ApiModel("工序生产进度vo")
public class ProcedureProgressVO {

    @ApiModelProperty("完成时间")
    private LocalDateTime endTime;

    @ApiModelProperty("完成人")
    private String completedBy;

    @ApiModelProperty("流程节点id")
    private String nodeId;

    @ApiModelProperty("流程状态")
    private Integer state;

    @ApiModelProperty("计划id")
    private Long planId;

    @ApiModelProperty("工序步骤模型id")
    private Long procedureModelId;

    @ApiModelProperty("状态枚举")
    private ProcessStateEnum stateEnum;

    @ApiModelProperty("换班类型")
    private String changeType;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("当前最新工序流程实例id")
    private String freshExecutionId;

    @ApiModelProperty("实例id集合")
    private List<String> executionIdList;

    public ProcessStateEnum getStateEnum(){
        return ProcessStateEnum.getEnumByValue(state);
    }

    public String getCompleteBy(){
        if (StrUtil.isBlank(completedBy)){
            return null;
        }
        BaseUserDO user = UserUtils.getUser(completedBy);
        return user.getLoginName() + StrUtil.DASHED + user.getUserName();
    }
}
