package com.bmos.lims2.server.inspect.item.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验项目(BmExperimentInspect)实体类
 *
 * @author makejava
 * @since 2024-03-02 12:45:18
 */
@Getter
@Setter
@TableName("lm_inspect_item")
public class InspectItem extends BaseDO {

    /**
     * 检验项目编码
     */
    private String code;
    /**
     * 检验项目名称
     */
    private String name;

    /**
     * 检验项目描述
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 是否为系统内置项目（0-否，1-是）
     * 系统内置项目不在前端列表中显示
     */
    private Boolean isSystem;

}

