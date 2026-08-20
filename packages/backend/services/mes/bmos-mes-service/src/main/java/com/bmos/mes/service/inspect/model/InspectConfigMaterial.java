package com.bmos.mes.service.inspect.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据权限表(BmInspectConfigMaterial)实体类
 *
 * @author makejava
 * @since 2025-02-13 13:42:42
 */
@Getter
@Setter
@TableName("bm_inspect_config_material")
public class InspectConfigMaterial {

    /**
     * 请验单配置id
     */
    private Long configId;

    /**
     * 物料id
     */
    private Long materialId;

}

