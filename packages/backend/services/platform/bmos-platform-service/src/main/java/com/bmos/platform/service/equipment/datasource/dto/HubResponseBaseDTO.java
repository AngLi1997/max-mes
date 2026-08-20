package com.bmos.platform.service.equipment.datasource.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * @author yigaohui
 * @date hub 相应baseDTO
 **/
@Data
public class HubResponseBaseDTO {

    public static final String SUCCESS_CODE = "0";

    private String code;

    private Object data;

    private String message;
}
