package com.bmos.mes.service.storage.config.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.storage.StorageLevelEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 暂存间
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:46
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_storage")
@Data
public class Storage extends BaseDO {

    /**
     * 上级区域id
     */
    private Long parentId;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 层级
     */
    private StorageLevelEnum level;
}
