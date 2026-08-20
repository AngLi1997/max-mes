package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 归档生成的批记录的操作日志(BmBatchRecordArchiveLog)实体类
 *
 * @author makejava
 * @since 2024-08-21 14:29:41
 */
@Getter
@Setter
@TableName("bm_batch_record_archive_log")
public class BatchRecordArchiveLog extends BaseDO {
    /**
     * bm_batch_record_archive 批记录模板版本表的主键id
     */
    private Long batchRecordArchiveId;
    /**
     * minio上传路径
     */
    private String path;
    /**
     * 操作类型
     * 830301-重新生成
     * 830302-上传
     * 830303-下载
     * 830304-提交审批
     * 830305-审批完成
     * 830306-作废
     * 830307-批记录生成
     */
    private Integer operateType;
    /**
     * 档案生成的时间
     */
    private LocalDateTime archiveTime;
    /**
     * 档案生效时间
     */
    private LocalDateTime effectiveTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 审核结果
     */
    private Boolean auditResult;
    /**
     * 审核意见
     */
    private String auditOpinion;
    /**
     * 审核流实例id
     */
    private String instanceId;
    /**
     * 节点名称
     */
    private String elementName;
    /**
     * 操作人ueseId
     */
    private String operatorId;
    /**
     * 操作人登录名称
     */
    private String operatorLoginName;
    /**
     * 操作人用户名称
     */
    private String operatorName;
    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

}

