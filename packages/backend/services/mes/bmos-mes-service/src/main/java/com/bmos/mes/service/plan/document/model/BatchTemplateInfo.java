package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板信息(BmBatchTemplateInfo)实体类
 *
 * @author makejava
 * @since 2024-08-19 11:06:02
 */
@Getter
@Setter
@TableName("bm_batch_template_info")
public class BatchTemplateInfo extends BaseDO {

    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类id bm_batch_template_category表的主键id
     */
    private Long categoryId;
}

