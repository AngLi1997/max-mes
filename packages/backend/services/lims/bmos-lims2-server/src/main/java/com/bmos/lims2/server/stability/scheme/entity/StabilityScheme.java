package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案实体类
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Getter
@Setter
@TableName("lm_stability_scheme")
public class StabilityScheme extends BaseDO {

    /**
     * 方案名称
     */
    private String name;

    /**
     * 方案编码
     */
    private String code;

    /**
     * 检品ID（物料ID）
     */
    private Long materialId;

    /**
     * 检品名称
     */
    private String materialName;

    /**
     * 检品编码
     */
    private String materialCode;

    /**
     * 当前生效版本号（冗余字段，便于列表快速判断和展示）
     */
    private String activeVersionNo;
}
