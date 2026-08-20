package com.bmos.mes.service.lotrelease.template.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批签发模板分类
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_lot_release_template_category")
public class LotReleaseTemplateCategory extends BaseDO {

    /**
     * 上级分类id
     */
    private Long parentId;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 数据集分类id路径
     */
    private String idPath;
}
