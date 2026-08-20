package com.bmos.mes.service.equipment.service.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yigaohui
 * @date 2024/4/23
 **/
@Data
public class EquipmentInfoComponentDTO extends BusinessDataHandleBaseDTO {
    private Long equipmentId;

    private String remark;
}
