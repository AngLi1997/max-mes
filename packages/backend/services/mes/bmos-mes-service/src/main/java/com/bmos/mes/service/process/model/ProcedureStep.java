package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 工序步骤信息实体
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure_step")
public class ProcedureStep {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工艺id
     */
    private Long processId;


    /**
     * 工序id
     */
    private Long procedureId;


    /**
     * 名称
     */
    private String name;

    /**
     * 步骤或者任务的区分
     */
    private StepTaskTypeEnum type;
}
