package com.bmos.lims2.server.inspect.entry.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 检项查询-表头分组行（参数-数据点）
 * @Author: yigaohui
 * @Date: 2025/09/12 10:00
 */
@Getter
@Setter
public class HeaderGroupRowDTO {

    private Long parameterId;
    private String parameterCode;
    private String parameterName;

    private String pointName;
    private String pointType;
}


