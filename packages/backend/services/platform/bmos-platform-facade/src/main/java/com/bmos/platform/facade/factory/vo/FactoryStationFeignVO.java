package com.bmos.platform.facade.factory.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备信息
 */
@Getter
@Setter
public class FactoryStationFeignVO{

    private Long id;

    /**
     * 工位code，用于唯一标识工位
     */
    private String code;
    /**
     * 工位名称，对工位的描述性文字
     */
    private String name;

}
