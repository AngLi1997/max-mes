package com.bmos.mes.service.dataset.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据集
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 14:28
 */
@TableName("bm_dataset")
@Data
@EqualsAndHashCode(callSuper = true)
public class Dataset extends BaseDO {

    /**
     * 数据集分类id
     */
    private Long datasetCategoryId;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 数据集类型 POINT 批记录数据(数据点) LOT_RELEASE_LINK 批签发引用 DYNAMIC_REPORT 动态数据填报
     * {@link DatasetType}
     */
    private DatasetType type;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 数据集key
     */
    private String datasetKey;
}
