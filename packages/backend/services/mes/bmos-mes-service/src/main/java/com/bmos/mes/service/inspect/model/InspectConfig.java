package com.bmos.mes.service.inspect.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单配置表(BmInspectConfig)实体类
 *
 * @author makejava
 * @since 2025-02-13 13:40:54
 */
@Getter
@Setter
@TableName("bm_inspect_config")
public class InspectConfig extends BaseDO {

    /**
     * 请验单名称
     */
    private String name;
    /**
     * 请验单的备注信息
     */
    private String remark;

    /**
     * 请验单是否启用
     */
    private Boolean enable;

    /**
     * 请验单最后修改人展示名称 username-loginname
     */
    private String updateShowName;

}

