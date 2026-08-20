package com.bmos.platform.service.equipment.datasource.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yigaohui
 * @date 2024/4/19
 * <p>
 * 向hub点位写入数据请求DTO
 **/
@Data
public class HubDataPointWriteRequestDTO {

    private List<HubDataPointValueDTO> params;
}
