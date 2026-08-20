package com.bmos.lims2.server.inspect.entry.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 检验数据点录入历史实体类
 *
 * @author system
 * @since 2025/01/30
 */
@Getter
@Setter
@TableName("lm_inspection_entry_history")
public class InspectionEntryHistory extends BaseDO {

    /**
     * 关联的录入记录ID
     */
    private Long entryRecordId;

    /**
     * 任务ID(冗余)
     */
    private Long taskId;

    /**
     * 检验单ID(冗余)
     */
    private Long inspectionOrderId;

    /**
     * 检验单号(冗余)
     */
    private String inspectionOrderNo;

    /**
     * 数据点ID(冗余)
     */
    private Long dataPointId;

    /**
     * 数据点配置ID(冗余)
     */
    private Long dataPointConfigId;

    /**
     * 方案ID(冗余)
     */
    private Long schemeId;

    /**
     * 方案版本ID(冗余)
     */
    private Long schemeVersionId;

    /**
     * 方案实验包ID(冗余)
     */
    private Long packageId;

    /**
     * 方案检验项目配置ID(冗余)
     */
    private Long itemConfigId;

    /**
     * 方案分析项配置ID(冗余)
     */
    private Long parameterConfigId;

    /**
     * 检验项目ID(冗余)
     */
    private Long inspectItemId;

    /**
     * 检验项目编码(冗余)
     */
    private String inspectItemCode;

    /**
     * 分析项ID(冗余)
     */
    private Long parameterId;

    /**
     * 分析项编码(冗余)
     */
    private String parameterCode;

    /**
     * 数据点名称(冗余)
     */
    private String dataPointName;

    /**
     * 数据点类型(冗余)
     */
    private DataPointTypeEnum pointType;

    /**
     * 旧文本/选项值
     */
    private String oldValueText;

    /**
     * 旧数值（字符串）
     */
    private String oldValueNumber;

    /**
     * 新文本/选项值
     */
    private String newValueText;

    /**
     * 新数值（字符串）
     */
    private String newValueNumber;

    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;
}
