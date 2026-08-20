package com.bmos.mes.service.plan.info.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.common.enums.plan.*;
import com.bmos.mes.service.workflow.vo.WorkflowTodoPageVO;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@With
@AllArgsConstructor
@ToString
@TableName(value = "bm_product_plan")
public class Plan extends BaseDO {
    @Tolerate
    public Plan() {
    }

    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate productDate;

    @ApiModelProperty("计划类型")
    private ProductPlanTypeEnum type;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("生产工艺数量")
    private Integer processNum;

    @ApiModelProperty("状态 编辑EDIT 审批中AUDIT 确认CONFIRM 废弃DISCARD")
    private ProductPlanStatusEnum status;

    @ApiModelProperty("状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND")
    private ProductPlanInstructStatusEnum instructStatus;

    @TableField("is_start")
    @ApiModelProperty("是否开始生产 未开始WAIT 已开始STARTING  结束 END")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private ProductPlanStartEnum start;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @TableField("is_relation")
    @ApiModelProperty("是否被其他批次关联 未关联FALSE 已关联TRUE")
    private BooleanEnum relation;

    @ApiModelProperty("流程实例")
    private String processInstanceId;

    @ApiModelProperty("生产执行流程实例")
    private String executeProcessInstanceId;

    @ApiModelProperty("确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;

    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("归档状态")
    private PlanArchiveStatusEnum archiveStatus;

    /**
     * 生产执行已暂停
     */
    private Boolean executePaused;

    /**
     * 生产计划组件修订数量
     */
    private Integer modifyCount;

    @TableField(exist = false)
    private String processModelId;

    @TableField(exist = false)
    private Long processVersionId;

    @TableField(exist = false)
    private Long exceptionCount;

    @ApiModelProperty("计划详情id")
    private Long productionPlanItemId;

    @ApiModelProperty("待办步骤/任务信息")
    @TableField(exist = false)
    private List<WorkflowTodoPageVO> todoPageVOList;

    @ApiModelProperty("产线名称")
    @TableField(exist = false)
    private String lineName;
}
