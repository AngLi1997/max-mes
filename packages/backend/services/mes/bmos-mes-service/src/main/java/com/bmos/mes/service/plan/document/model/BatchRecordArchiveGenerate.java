package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * (BmBatchRecordArchiveGenerate)实体类
 *
 * @author makejava
 * @since 2024-08-27 18:51:41
 */
@Getter
@Setter
@TableName("bm_batch_record_archive_generate")
public class BatchRecordArchiveGenerate extends BaseDO {

    /**
     * 模板版本id bm_batch_template_version的主键id
     */
    private Long batchTemplateVersionId;
    /**
     * 生产计划id
     */
    private Long planId;
    /**
     * 若为重新生成，则此值为bm_batch_record_archive表中的id
     */
    private Long batchRecordArchiveId;
    /**
     * 操作人id
     */
    private String userId;
    /**
     * 是否生成完成
     */
    private Boolean complete;
    /**
     * {@link com.bmos.mes.common.enums.plan.BatchRecordArchiveOperateTypeEnum}
     */
    private Integer operateType;
    /**
     * 归档生成的批记录的path
     */
    private String path;

    /**
     * 生成的文件是否在minio中否被删除
     */
    private Boolean deleteFileFlag;

}

