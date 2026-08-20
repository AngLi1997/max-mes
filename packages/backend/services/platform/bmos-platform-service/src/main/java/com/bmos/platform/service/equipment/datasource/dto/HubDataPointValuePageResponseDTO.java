package com.bmos.platform.service.equipment.datasource.dto;

import lombok.Data;

import java.util.List;

/**
 * hub数据分页返回DTO
 *
 * @author yigaohui
 * @date 2024/4/24
 **/
@Data
public class HubDataPointValuePageResponseDTO {
    private Integer current;

    private Integer pages;

    private Integer size;

    private Integer total;

    private List<HubDataPointPageValueDTO> records;
}
