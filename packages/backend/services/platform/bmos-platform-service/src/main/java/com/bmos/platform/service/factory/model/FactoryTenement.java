package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 楼宇(BpFactoryTenement)实体类
 *
 * @author makejava
 * @since 2024-12-30 11:54:58
 */
@Data
@TableName("bp_factory_tenement")
public class FactoryTenement extends BaseDO {
    private static final long serialVersionUID = 949045974991511772L;
    /**
     * 楼宇编码
     */
    private String code;
    /**
     * 楼宇名称
     */
    private String name;

    /**
     * 父级id
     */
    private Long parentId;
}

