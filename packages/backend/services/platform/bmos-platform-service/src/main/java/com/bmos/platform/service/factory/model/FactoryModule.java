package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 设备工厂模型表，记录设备工厂相关的模块信息(BpEquipmentModule)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:37:12
 */
@Getter
@Setter
@TableName("bp_factory_module")
public class FactoryModule extends BaseDO implements Serializable {
    /**
     * 模型编码，用于唯一、简洁地标识模型
     */
    private String code;
    /**
     * 模型名称，对模型的描述性文字
     */
    private String name;
    /**
     * 模型类型，用整数表示不同的模型类别
     */
    private Integer type;
    /**
     * 当前模型上级ID，若无上级则为0
     */
    private Long parentId;

}

