package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板版本(BmBatchTemplateVersion)实体类
 *
 * @author makejava
 * @since 2024-08-19 11:07:01
 */
@Getter
@Setter
@TableName("bm_batch_template_version")
public class BatchTemplateVersion extends BaseDO {

    /**
     * 版本号
     */
    private String version;

    /**
     * 模板minio路径
     */
    private String path;
    /**
     * 模板备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否设为默认
     */
    private Boolean normal;
    /**
     * 批记录模板id  bm_batch_template_info表的主键id
     */
    private Long batchTemplateInfoId;

}

