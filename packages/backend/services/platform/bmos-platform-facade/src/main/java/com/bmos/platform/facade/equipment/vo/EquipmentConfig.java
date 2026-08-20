package com.bmos.platform.facade.equipment.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.facade.equipment.enums.EquipmentProto;
import com.bmos.platform.facade.equipment.enums.EquipmentType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 09:21
 */
@Data
@ApiModel("设备配置")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentConfig {

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", example = "1")
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", example = "电子天平")
    private String equipmentName;

    /**
     * 设备类型
     */
    @ApiModelEnumProperty(value = "设备类型", enumClass = EquipmentType.class)
    @EnumValidate(value = EquipmentType.class)
    private EquipmentType type;

    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编码", example = "B01")
    private String equipmentCode;

    /**
     * 设备协议
     */
    @ApiModelEnumProperty(value = "设备协议", enumClass = EquipmentProto.class)
    @EnumValidate(value = EquipmentProto.class)
    private EquipmentProto equipmentProto;

    /**
     * 设备地址
     */
    @ApiModelProperty(value = "设备请求地址", example = "ws://192.168.110.254:8000")
    private String url;
}
