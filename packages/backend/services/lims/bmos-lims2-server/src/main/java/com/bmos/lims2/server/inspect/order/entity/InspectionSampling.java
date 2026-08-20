package com.bmos.lims2.server.inspect.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.SamplingStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;


/**
 * 检验取样计划实体类（请验阶段生成的取样计划）
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@TableName("lm_inspection_sampling")
public class InspectionSampling extends BaseDO {

    /**
     * 检验单ID
     */
    private Long inspectionOrderId;

    /**
     * 检验项目ID（可为空，代表整体取样）
     */
    private Long inspectItemId;

    /**
     * 计划取样量（字符串存储）
     */
    private String plannedQuantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 计划取样份数
     */
    private Integer sampleCount;

    /**
     * 取样方式
     */
    private String samplingMethod;

    /**
     * 取样地点
     */
    private String samplingLocation;

    /**
     * 取样说明
     */
    private String samplingDescription;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关联的样品ID（请验阶段生成样品后设置）
     */
    private Long sampleId;

    /**
     * 关联的样品编号（请验阶段生成样品后设置）
     */
    private String sampleNo;
    
    // 注意：实际取样量、实际取样份数、取样状态等字段
    // 由后续的样品管理功能维护，不在请验阶段处理
}