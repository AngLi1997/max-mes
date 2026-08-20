package com.bmos.mes.service.process.vo;

import lombok.Data;

@Data
public class ProductLineVO {

    /**
     * 产线id
     */
    private Long id;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 产线名称
     */
    private String name;
}
