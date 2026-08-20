package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板分类(BmBatchTemplateCategory)实体类
 *
 * @author makejava
 * @since 2024-08-19 11:05:35
 */
@Getter
@Setter
@TableName("bm_batch_template_category")
public class BatchTemplateCategory extends BaseDO {

    /**
     * 分类名称
     */
    private String name;
    /**
     * 父级分类id
     */
    private Long parentId;

}

