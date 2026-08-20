package com.bmos.platform.service.factory.service.dto;

import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 楼宇楼层表(BpTenementFloor)实体类
 *
 * @author makejava
 * @since 2024-12-30 14:09:26
 */
@Data
public class FactoryTenementFloorDTO {

    private Long id;
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

    private String createBy;


    private String updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

