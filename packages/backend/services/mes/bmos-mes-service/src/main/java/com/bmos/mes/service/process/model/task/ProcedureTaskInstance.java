package com.bmos.mes.service.process.model.task;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_procedure_task_instance")
public class ProcedureTaskInstance extends BaseDO {

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("计划id")
    private Long planId;

    @ApiModelProperty("任务模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("任务流程状态")
    private String flowState;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("记录重做标识")
    private String type;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("开始时间")
    @TableField(updateStrategy=FieldStrategy.IGNORED)
    private LocalDateTime startTime;

    @ApiModelProperty("完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty("工艺次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("强制开启人")
    private String coerceUser;

    @ApiModelProperty("暂停标识")
    private String pauseTag;

    @ApiModelProperty("工序节点id")
    @TableField(exist = false)
    private String nodeId;

    @ApiModelProperty("工序名称")
    @TableField(exist = false)
    private String procedureName;

    @ApiModelProperty("强制开启时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime coerceTime;

    @ApiModelProperty("激活时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime activeTime;


    @ApiModelProperty("状态，只返回已完成或者进行中")
    @TableField(exist = false)
    private Integer state;

    public Integer getState(){
        //1进行中
        if (StrUtil.equals(flowState,ProductTaskStatusEnum.ACTIVATED.getValue())){
            return 1;
        }
        //已完成或者已结束
        if (StrUtil.equals(flowState,ProductTaskStatusEnum.COMPLETE.getValue())){
            return ProcessConstant.IS_END.equals(type) ? 3 : 4;
        }

        //0未激活
        if (StrUtil.equals(flowState,ProductTaskStatusEnum.DISABLE.getValue())){
            return 0;
        }
        //2已激活
        if (StrUtil.equals(flowState,ProductTaskStatusEnum.ENABLE.getValue())){
            return 2;
        }
        return null;
    }


}
