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

/**
 * @ClassName StepAndTaskProgressVO
 * @Description 工步以及任务生产进度数据vo
 * @Author Ren Jin Guang
 * @Date 2024/8/26 11:08
 */
@Setter
@Getter
@ToString
@ApiModel("工步以及任务生产进度数据vo")
public class TaskProgressVO {

    @ApiModelProperty("完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty("完成人")
    private String updateBy;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("任务状态")
    private String flowState;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("状态枚举")
    private ProcessStateEnum stateEnum;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("状态")
    private Integer state;

    public ProcessStateEnum getStateEnum() {
        return ProcessStateEnum.getEnumByValue(state);
    }

    public String getUpdateBy() {
        if (StrUtil.isBlank(updateBy)) {
            return null;
        }
        BaseUserDO user = UserUtils.getUser(updateBy);
        return user.getLoginName() + StrUtil.DASHED + user.getUserName();
    }

}
