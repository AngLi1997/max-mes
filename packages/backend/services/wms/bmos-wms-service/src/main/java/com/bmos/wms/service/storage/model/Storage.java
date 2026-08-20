package com.bmos.wms.service.storage.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.wms.common.enums.inventory.StorageLevelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 存储区域
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:46
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bw_storage")
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
