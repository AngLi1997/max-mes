package com.bmos.platform.service.equipment.datasource.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yigaohui
 * @date hub 数据点位值的DTO
 **/
@Data
@Accessors(chain = true)
public class HubDataPointPageValueDTO {

    private String tagId;

    private String tagName;

    private String q;

    private Long ts;

    private String value;

    private Long createTs;
}
