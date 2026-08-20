package com.bmos.mes.service.dataset.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据集分类
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 14:29
 */
@TableName("bm_dataset_category")
@Data
@EqualsAndHashCode(callSuper = true)
public class DatasetCategory extends BaseDO {

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
