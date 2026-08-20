package com.bmos.mes.service.equipment.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * (BmProcedureEquipmentInfo)实体类
 *
 * @author makejava
 * @since 2024-04-23 14:08:27
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure_equipment_info")
public class ProcedureEquipmentInfo extends BaseDO implements Serializable {
    private static final long serialVersionUID = -25647626127875448L;

    /**
     * 生产计划id
     */
    private Long productPlanId;
    /**
     * 批次号
     */
    private String batchNo;

    private Long processId;

    private String processVersion;

    private Long recordItemId;

    private Long recordVersionId;

    private Long procedureStepId;
    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;
    /**
     * 组件id
     */
    private Long componentId;
    /**
     * 设备id
     */
    private Long equipmentId;

}

