package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.util.Set;

/**
 * @className: EquipmentDataPropertyQueryVO
 * @author: yigaohui
 * @date: 2024/11/29 14:41
 * @Version: 1.0
 * @description:
 */
@Data
@ApiModel(value = "设备数据属性查询参数")
public class EquipmentDataPropertyQueryVO {

    @ApiModelProperty(value = "采集平台")
    private AcquisitionPlatformEnum acquisitionPlatform;

    @ApiModelProperty(value = "设备数据属性编码集合")
    private Set<String> codeSet;
}
