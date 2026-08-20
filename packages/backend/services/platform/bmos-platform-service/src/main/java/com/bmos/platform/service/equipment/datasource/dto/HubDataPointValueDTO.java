package com.bmos.platform.service.equipment.datasource.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yigaohui
 * @date hub 数据点位值的DTO
 **/
@Data
@Accessors(chain = true)
public class HubDataPointValueDTO {
    private String id;

    private String name;

    private String q;

    private Long ts;

    private String v;
}
