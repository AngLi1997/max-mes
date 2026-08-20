package com.bmos.platform.service.equipment.datasource.dto;

import lombok.Data;

/**
 * @author yigaohui
 * @date 2024/4/19
 * <p>
 * 向hub点位写入数据返回值DTO
 **/
@Data
public class HubDataPointWriteResponseDTO extends HubDataPointValueDTO {

    private String code;

    private String msg;
}
