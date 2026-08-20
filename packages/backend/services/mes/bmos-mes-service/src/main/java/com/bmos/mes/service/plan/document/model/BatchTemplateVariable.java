package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板中配置的所有变量(BmBatchTemplateVariable)实体类
 *
 * @author makejava
 * @since 2024-08-19 14:12:41
 */
@Getter
@Setter
@TableName("bm_batch_template_variable")
public class BatchTemplateVariable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 占位符
     */
    private String placeholder;
    /**
     * 模板信息id  表bm_batch_template_version的主键id
     */
    private Long batchTemplateVersionId;


}

