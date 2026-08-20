package com.bmos.mes.service.inspect.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单信息表(BmInspectInfo)实体类
 *
 * @author makejava
 * @since 2025-02-17 18:25:22
 */
@TableName("bm_inspect_info")
@Getter
@Setter
public class InspectInfo extends BaseDO {

    /**
     * 请验单主键id bm_inspect表主键
     */
    private Long inspectId;
    /**
     * 请验单配置数据id bm_inspect_config_data表主键
     */
    private Long inspectConfigDataId;
    /**
     * 请验单数据code 内置数据code前端定义，若是字典数据code则为字典的value
     */
    private String code;
    /**
     * 展示名称
     */
    private String showName;
    /**
     * 请验单数据名称
     */
    private String dataName;
    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 所填的值
     */
    private String value;
    /**
     * 排序 请验详情时前端显示的排序
     */
    private Integer sort;
}

