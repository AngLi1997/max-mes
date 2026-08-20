package com.bmos.platform.service.factory.service.dto;

import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 楼宇楼层表(BpTenementFloor)实体类
 *
 * @author makejava
 * @since 2024-12-30 14:09:26
 */
@Data
public class FactoryTenementListQueryDTO {

    @ApiModelProperty("楼层名称")
    private String name;

    @ApiModelProperty("楼层编码")
    private String code;

    @ApiModelProperty("楼栋id")
    private List<Long> tenementIds;

    @ApiModelEnumProperty(value = "楼栋状态", enumClass = TenementFloorStatusEnums.class)
    private TenementFloorStatusEnums status;

}

