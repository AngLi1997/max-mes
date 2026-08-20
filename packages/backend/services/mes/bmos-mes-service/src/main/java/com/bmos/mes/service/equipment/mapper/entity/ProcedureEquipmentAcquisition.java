package com.bmos.mes.service.equipment.mapper.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.equipment.service.enums.EquipmentAcquisitionComponentInputTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 工序步骤设备数据采集数据(BmProcedureEquipmentAcquisition)实体类
 *
 * @author makejava
 * @since 2024-04-23 14:07:05
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure_equipment_acquisition")
public class ProcedureEquipmentAcquisition extends BaseDO implements Serializable {

    private Long componentId;


    private Long productPlanId;


    private String batchNo;


    private Long processId;


    private String processVersion;


    private Long recordItemId;


    private Long recordVersionId;


    private Long procedureStepId;

    private Long procedureStepModelId;

    private Long copyVersion;

    private Long equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备编码
     */
    private String equipmentCode;

    private Long acquisitionId;

    private String acquisitionCode;

    private String dataPointName;

    private String dataPointValue;

    private LocalDateTime dataPointValueTime;

    private LocalDateTime acquisitionTime;

    private EquipmentAcquisitionComponentInputTypeEnum inputType;

    /**
     * 记录采集数据的顺序
     * 同一组件中同次采集多个数据点采集的数据该值相同
     */
    private Integer acquisitionSort;

    /**
     * 是否复用 用于数据查询
     */
    private Boolean reuse;

    /**
     * 分组id
     * 表格只填入从表格分组进入执行的数据
     */
    private Long groupComponentId;

    /**
     * 设备数据编码
     * 数采点对应设备数据自定义字段数据键值
     */
    private String dataDictCode;

}

