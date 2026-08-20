package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * 楼宇楼层表(BpTenementFloor)实体类
 *
 * @author makejava
 * @since 2024-12-30 14:09:26
 */
@TableName("bp_factory_tenement_floor")
@Data
public class FactoryTenementFloor extends BaseDO implements Serializable {
    private static final long serialVersionUID = 737324438786578255L;
    /**
     * 楼栋id
     */
    private Long tenementId;
    /**
     * 编码
     */
    private String code;
    /**
     * 楼层名称
     */
    private String name;
    /**
     * 状态
     */

    private TenementFloorStatusEnums status;

}

