package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 23:01
 */
@Data
@ApiModel("扫码设备信息")
public class ScanDeviceVO {

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", example = "1")
    private Long deviceId;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号", example = "001")
    private String deviceCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", example = "不锈钢盆儿")
    private String deviceName;

    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号", example = "WH0501301")
    private String deviceSpecification;

    /**
     * 设备厂商
     */
    @ApiModelProperty(value = "设备厂商", example = "百墨思")
    private String deviceManufacturer;
}
