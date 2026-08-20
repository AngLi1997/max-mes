package com.bmos.lims2.server.inspect.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 检验单据实体类
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@TableName("lm_inspection_order")
public class InspectionOrder extends BaseDO {

    /**
     * 检验单号
     */
    private String orderNo;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检验方案版本ID
     */
    private Long schemeVersionId;

    /**
     * 单据状态：待确认、已确认、已终止
     * 请验阶段只涉及这三种状态，后续状态由其他功能管理
     */
    private InspectionOrderStatusEnum orderStatus;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime productionDate;

    /**
     * 请验单模板ID
     */
    private Long templateId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 终止原因
     */
    private String terminateReason;

    /**
     * 终止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime terminateTime;

    /**
     * 样品审核完成时间
     */
    private LocalDateTime sampleAuditTime;

    /**
     * 样品审核流程实例ID
     */
    private String sampleAuditProcessInstanceId;

    /**
     * 请验是否完成
     */
    private Boolean finished;

    /**
     * 请验完成时间
     */
    private LocalDateTime finishedTime;

    /**
     * 修改次数
     */
    private Integer modifyCount;

    /**
     * 是否需要留样
     */
    private Boolean retentionRequired;

    /**
     * 有效期至（留样时必填）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate retentionExpiryDate;

    /**
     * 检验单来源（REGULAR=常规请验；STABILITY=稳定性考察时间点任务自动创建）
     * 内部标志位，不对前端暴露
     */
    private InspectionOrderSourceEnum schemeSource;

    /**
     * 上游来源系统：MES / WMS（默认 MES，向后兼容历史数据）。
     * 由 MesInspectAdapter 在建单时落库，audit 流程结束后据此选择回调到 MES 还是 WMS。
     */
    private String sourceSystem;
}