package com.bmos.lims2.server.inspect.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 样品实体类
 * 注意：此实体由样品管理功能维护，请验阶段不直接操作
 * 样品的创建、状态更新等由样品登记、接收、领取等功能处理
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@TableName("lm_sample")
public class Sample extends BaseDO {

    /**
     * 检验单ID
     */
    private Long inspectionOrderId;

    /**
     * 稳定性考察计划样品ID（整体取样时关联 lm_stability_plan_sample.id，检验单样品时为空）
     */
    private Long stabilityPlanSampleId;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 样品名称
     */
    private String sampleName;

    /**
     * 是否已取样
     */
    private Boolean sampled;

    /**
     * 是否已接收
     */
    private Boolean received;

    /**
     * 是否已分样
     */
    private Boolean divided;

    /**
     * 是否已领取
     */
    private Boolean collected;

    /**
     * 是否作废
     */
    private Boolean discarded;

    /**
     * 是否已销毁
     */
    private Boolean destroyed;

    /**
     * 检验项目ID
     */
    private Long inspectItemId;

    /**
     * 父样品ID（分样时使用）
     */
    private Long parentSampleId;


    /**
     * 父样品编号
     */
    private String parentSampleNo;

    /**
     * 样品数量（字符串存储）
     */
    private String planQuantity;

    /**
     * 样品数量（字符串存储）
     */
    private String quantity;

    /**
     * 当前样品数量（默认等于quantity，领用后会减少）
     */
    private String currentQuantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 取样人
     */
    private String samplerName;

    /**
     * 取样人ID
     */
    private String samplerId;

    /**
     * 取样时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime samplingTime;

    private String receiverId;


    /**
     * 接收人
     */
    private String receiverName;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    /**
     * 分样人
     */
    private String dividerName;

    /**
     * 分样时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime divideTime;

    /**
     * 领取人ID
     */
    private String collectorId;

    /**
     * 领取人
     */
    private String collectorName;

    /**
     * 领取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;

    /**
     * 作废人
     */
    private String discardedBy;

    /**
     * 作废时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime discardedTime;

    /**
     * 作废原因
     */
    private String discardedReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已回收
     */
    private Boolean recycled;

    /**
     * 回收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recycledTime;

    /**
     * 回收人
     */
    private String recycledBy;

    /**
     * 回收余量（字符串存储）
     */
    private String recycleQuantity;

    /**
     * 回收余量单位ID
     */
    private Long recycleUnitId;

    /**
     * 回收备注
     */
    private String recycleRemark;

    /**
     * 消耗量（字符串存储）
     */
    private String consumedQuantity;

    /**
     * 是否已处理
     */
    private Boolean processed;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processTime;

    /**
     * 处理人
     */
    private String processBy;

    /**
     * 处理方式
     */
    private String processMethod;

    /**
     * 处理备注
     */
    private String processRemark;

    /**
     * 标签是否已打印
     */
    private Boolean tagPrinted;

    /**
     * 储存位置
     */
    private String storageLocation;

    /**
     * 留样期限（有效期至+1年）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate retentionExpiryDate;
}