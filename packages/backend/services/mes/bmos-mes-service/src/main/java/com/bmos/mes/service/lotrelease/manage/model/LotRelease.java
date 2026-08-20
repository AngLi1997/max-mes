package com.bmos.mes.service.lotrelease.manage.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.lotrelease.manage.enums.LotReleaseStatus;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 18:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_lot_release")
public class LotRelease extends BaseDO {

    /**
     * 批签发编号
     */
    private String no;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板版本
     */
    private String templateVersion;

    /**
     * 模版id
     */
    private Long templateId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 产品名称
     */
    private String processName;

    /**
     * 产品合并编码
     */
    private String productMergeCode;

    /**
     * 规格
     */
    private String specification;

    /**
     * 生产计划id
     */
    private Long planId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 提交审核人姓名
     */
    private String submitterName;

    /**
     * 提交审核人id
     */
    private String submitterId;

    /**
     * 提交审核时间
     */
    private LocalDateTime submitterTime;

    /**
     * 审核流程实例id
     */
    private String auditProcessInstance;

    /**
     * 生成人姓名
     */
    private String generatorName;

    /**
     * 生成人id
     */
    private String generatorId;

    /**
     * 生成时间
     */
    private LocalDateTime generateTime;

    /**
     * 生效时间
     */
    private LocalDateTime effectTime;

    /**
     * 批签发状态
     */
    private LotReleaseStatus status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文件地址
     */
    private String fileUrl;
}
