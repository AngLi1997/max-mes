package com.bmos.lims2.server.inspect.item.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验项与分析项关联表(BmExperimentInspectAnalyze)实体类
 *
 * @author makejava
 * @since 2024-03-02 12:48:54
 */
@Getter
@Setter
@TableName("lm_item_parameter")
public class InspectItemParameter extends BaseDO {

    /**
     * 检验项id
     */
    private Long inspectItemId;
    /**
     * 分析项id
     */
    private Long inspectParameterId;
}

