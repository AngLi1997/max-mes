package com.bmos.mes.service.process.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@ApiModel("工序步骤DTO")
public class ProcedureStepDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("工序步骤id")
    private Long procedureStepId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 若未传历史工步id
     * 使用该名称创建新的历史工步
     */
    @ApiModelProperty(value = "历史工步名称", required = true)
    private String historicalName;

    /**
     * 节点功能
     */
    @ApiModelProperty("工序功能")
    private String nodeFunction;


    /**
     * 是否可复用
     */
    @ApiModelProperty("是否可复用")
    private Boolean reusable;

    /**
     * 时长
     */
    @ApiModelProperty("时长")
    private Long duration;

    /**
     * 流程节点id
     */
    @ApiModelProperty(value = "流程节点id", required = true)
    private String nodeId;


    @ApiModelProperty("操作规程id可多选")
    private List<Long> operationSopId;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String timeUnit;


    @ApiModelProperty("记录项id")
    private Long recordItemId;


    @ApiModelProperty("批记录版本id")
    private Long recordVersionId;

    @ApiModelProperty("执行岗")
    private List<Long> roles;

    @ApiModelProperty("区域")
    private List<Long> areaList;

    @ApiModelProperty("设备类")
    private List<Long> equipmentTypeList;

    @ApiModelProperty("执行条件")
    private ExpressionSaveDTO executeCondition;

    @ApiModelProperty("完成条件")
    private ExpressionSaveDTO completeCondition;

    @JsonIgnore
    @ApiModelProperty("步骤或者任务的区分")
    private StepTaskTypeEnum stepType;

    @JsonIgnore
    public void validated(Boolean type) {
        if (StrUtil.isEmpty(name) || StrUtil.isEmpty(nodeId) || StrUtil.isEmpty(nodeFunction)
                || ((Objects.isNull(reusable) || Objects.isNull(recordVersionId) || Objects.isNull(recordItemId)) && !ProcedureStepNodeFunctionEnum.notRecordNode(nodeFunction))) {
                if (type) {
                    throw new BmosException(MesResponseCode.PROCESS_TASK_NOT_FINISH);
                }
                throw new BmosException(MesResponseCode.PROCESS_STEP_NOT_FINISH);
        }
        if (executeCondition != null) {
            if (executeCondition.getConditionList().size() > 20) {
                throw new BmosException(MesResponseCode.PROCESS_CONDITION_ERROR);
            }
        }
        if (completeCondition != null) {
            if (completeCondition.getConditionList().size() > 20) {
                throw new BmosException(MesResponseCode.PROCESS_CONDITION_ERROR);
            }
        }
    }
}
