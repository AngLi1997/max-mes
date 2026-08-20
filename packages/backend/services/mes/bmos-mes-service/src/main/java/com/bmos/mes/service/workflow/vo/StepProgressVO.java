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
 * @ClassName StepProgressVO
 * @Description 工步生产进度数据vo
 * @Author Ren Jin Guang
 * @Date 2024/8/26 11:08
 */
@Setter
@Getter
@ToString
@ApiModel("工步生产进度数据vo")
public class StepProgressVO {

    @ApiModelProperty("完成时间")
    private LocalDateTime endTime;

    @ApiModelProperty("完成人")
    private String completeBy;

    @ApiModelProperty("流程节点id")
    private String nodeId;

    @ApiModelProperty("流程状态")
    private Integer state;

    @ApiModelProperty("激活状态")
    private Boolean activeState;

    @ApiModelProperty("状态枚举")
    private ProcessStateEnum stateEnum;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    public ProcessStateEnum getStateEnum() {
        return ProcessStateEnum.getEnumByValue(state);
    }

    public String getCompleteBy() {
        if (StrUtil.isBlank(completeBy)) {
            return null;
        }
        BaseUserDO user = UserUtils.getUser(completeBy);
        return user.getLoginName() + StrUtil.DASHED + user.getUserName();
    }

}
