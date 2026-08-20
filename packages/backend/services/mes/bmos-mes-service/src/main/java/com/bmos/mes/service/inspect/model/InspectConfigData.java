package com.bmos.mes.service.inspect.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单配置数据表(BmInspectConfigData)实体类
 *
 * @author makejava
 * @since 2025-02-13 13:42:21
 */
@Getter
@Setter
@TableName("bm_inspect_config_data")
public class InspectConfigData extends BaseDO {

    /**
     * 请验单配置表id bm_inspect_config表主键
     */
    private Long configId;
    /**
     * 请验单展示数据名称
     */
    private String showName;
    /**
     * 请验单数据code 内置数据code前端定义，若是字典数据code则为字典的value
     */
    private String code;
    /**
     * 请验单数据名称
     */
    private String dataName;
    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 排序 同一个config_id下的请验单数据在前端的显示顺序
     */
    private Integer sort;

}

