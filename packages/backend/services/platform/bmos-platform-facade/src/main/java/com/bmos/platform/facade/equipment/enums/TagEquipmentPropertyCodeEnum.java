package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagEquipmentPropertyCodeEnum implements CommonEnum<String> {

    /**
     * 称量单位
     */
    WEIGHING_UNIT("WEIGHING_UNIT_001", "称量单位"),
    /**
     * 称量精度
     */
    WEIGHING_ACCURACY("WEIGHING_ACCURACY_002", "称量精度"),
    /**
     * 称量范围
     */
    WEIGHING_RANGE("WEIGHING_RANGE_003", "称量范围"),

    /**
     * 容器皮重
     */
    CONTAINER_WEIGHT("CONTAINER_WEIGHT_004", "容器皮重"),
    /**
     * 内容物体积
     */
    CONTENT_VOLUME("CONTENT_VOLUME_005", "内容物体积"),
    /**
     * 内容物重量
     */
    CONTENT_WEIGHT("CONTENT_WEIGHT_006", "内容物重量"),
    /**
     * IP地址
     */
    IP_ADDRESS("IP_ADDRESS_007", "IP地址"),
    /**
     * 端口
     */
    PORT("PORT_008", "端口"),
    /**
     * 资产编码
     */
    ASSET_CODE("ASSET_CODE_009", "资产编码"),
    /**
     * PAD地址
     */
    PAD_ADDRESS("PAD_ADDRESS_010", "PAD地址"),
    /**
     * 打印机dpi
     */
    PRINTER_DPI("PRINTER_DPI_011", "打印机DPI"),

    /**
     * 称具通信协议类型
     */
    WEIGHING_PROTOCOL_TYPE("WEIGHING_PROTOCOL_TYPE_012", "称具通信协议类型")
    ;
    @EnumValue
    private String code;

    private String name;


    @Override
    public String getValue() {
        return "";
    }
}
