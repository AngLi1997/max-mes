package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案检验项目配置实体类
 *
 * @author yigaohui
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("lm_stability_scheme_item")
public class StabilitySchemeItem extends BaseDO {

    private Long schemeId;

    private Long versionId;

    private Long inspectItemId;

    private Integer duration;

    private ItemDurationUnitEnum timeUnit;
}
