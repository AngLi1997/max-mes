package com.bmos.lims2.server.inspect.entry.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 检验数据点录入记录实体类
 *
 * @author system
 * @since 2025/01/30
 */
@Getter
@Setter
@TableName("lm_inspection_entry_record")
public class InspectionEntryRecord extends BaseDO {

    /**
     * 检验单ID
     */
    private Long inspectionOrderId;

    /**
     * 检验单号(冗余)
     */
    private String inspectionOrderNo;

    /**
     * 分析项任务ID
     */
    private Long taskId;

    /**
     * 方案ID(冗余)
     */
    private Long schemeId;

    /**
     * 方案版本ID(冗余)
     */
    private Long schemeVersionId;

    /**
     * 方案实验包配置ID(冗余)
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
     * 数据点配置ID(来自lm_inspection_scheme_data_point表)
     */
    private Long dataPointConfigId;

    /**
     * 数据点ID(基础配置)
     */
    private Long dataPointId;

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
     * 数据点类型：NUMBER/TEXT/OPTION
     */
    private DataPointTypeEnum pointType;

    /**
     * 文本值/选项值
     */
    private String valueText;

    /**
     * 数值型结果（以字符串存储，确保与录入值一致）
     */
    private String valueNumber;

    /**
     * 检验时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime testTime;

    /**
     * 录入人ID
     */
    private String operatorId;

    /**
     * 录入人姓名
     */
    private String operatorName;

    /**
     * 是否判定异常(冗余，便于筛选)
     */
    @TableField("is_abnormal")
    private Boolean abnormal;

    /**
     * 备注
     */
    private String remark;
}
