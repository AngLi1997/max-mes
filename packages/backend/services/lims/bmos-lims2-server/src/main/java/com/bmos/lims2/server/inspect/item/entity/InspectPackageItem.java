package com.bmos.lims2.server.inspect.item.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 实验包与检验项关联表(BmExperimentPackageInspect)实体类
 *
 * @author makejava
 * @since 2024-03-02 12:50:48
 */
@Getter
@Setter
@TableName("lm_package_item")
public class InspectPackageItem extends BaseDO {

    /**
     * 实验包id
     */
    private Long inspectPackageId;
    /**
     * 检验项id
     */
    private Long inspectItemId;
}

