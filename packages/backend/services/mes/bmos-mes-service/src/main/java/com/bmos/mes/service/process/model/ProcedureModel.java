package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 工序信息实体
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure_model")
public class ProcedureModel extends BaseDO {

    private String name;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺版本号
     */
    private String processVersion;

    /**
     * 工艺版本id
     */
    private Long processVersionId;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 流程模型id
     */
    private String processModelId;

    /**
     * 负责人
     */
    private Long principal;

    /**
     * 时长
     */
    private Long duration;

    /**
     * 单位
     */
    private String timeUnit;

    /**
     * 排序号
     */
    private Integer sort;


    /**
     * 阶段编码
     */
    private String stageCode;

    @TableField(exist = false)
    private List<Long> groupIds;

    @TableField(exist = false)
    private List<Long> formulaMaterialIdList = new ArrayList<>();

    @TableField(exist = false)
    private List<String> roomIdList = new ArrayList<>();

    @TableField(exist = false)
    private ExpressionSaveDTO completeCondition;

    private Long delIdFlag;

    @TableField(exist = false)
    /**
     * 历史模型id
     */
    private Long historyModelId;
}
