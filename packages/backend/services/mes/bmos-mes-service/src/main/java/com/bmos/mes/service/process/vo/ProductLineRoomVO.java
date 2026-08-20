package com.bmos.mes.service.process.vo;

import lombok.Data;

@Data
public class ProductLineRoomVO {
    /**
     * 房间id
     */
    private Long id;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 房间编码
     */
    private String code;
}
