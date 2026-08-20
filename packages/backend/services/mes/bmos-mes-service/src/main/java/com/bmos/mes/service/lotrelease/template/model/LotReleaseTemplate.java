package com.bmos.mes.service.lotrelease.template.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批签发模板
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_lot_release_template")
public class LotReleaseTemplate extends BaseDO {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 生效批签发id
     */
    private Long effectiveLotReleaseId;
}
