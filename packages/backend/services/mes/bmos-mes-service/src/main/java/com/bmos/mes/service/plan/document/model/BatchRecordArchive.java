package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 归档生成的批记录档案(BmBatchRecordArchive)实体类
 *
 * @author makejava
 * @since 2024-08-21 11:28:03
 */
@Getter
@Setter
@TableName("bm_batch_record_archive")
public class BatchRecordArchive extends BaseDO {

    /**
     * 批记录编号
     */
    private String archiveNo;
    /**
     * minio上传路径
     */
    private String path;
    /**
     * 批记录模板id
     */
    private Long batchTemplateInfoId;
    /**
     * 当前生成的批记录是由哪一个模板版本id生成的
     */
    private Long batchTemplateVersionId;
    /**
     * 模板名曾
     */
    private String templateName;
    /**
     * 模板版本
     */
    private String templateVersion;
    /**
     * 生产计划id
     */
    private Long planId;
    /**
     * 生产批号
     */
    private String batchNo;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 审核流实例id
     */
    private String instanceId;
    /**
     * 状态 830301-编辑 830302-审批中 830303-生效 830303 - 作废
     */
    private Integer status;
    /**
     * 审核人id
     */
    private String auditorId;
    /**
     * 审核人名称
     */
    private String auditorName;
    /**
     * 审核人登录名称
     */
    private String auditorLoginName;
    /**
     * 归档时间（生成时间）
     */
    private LocalDateTime archiveTime;
    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 操作人ueseId
     */
    private String operatorId;
    /**
     * 操作人用户名称
     */
    private String operatorName;
    /**
     * 操作人登录名称
     */
    private String operatorLoginName;

}

