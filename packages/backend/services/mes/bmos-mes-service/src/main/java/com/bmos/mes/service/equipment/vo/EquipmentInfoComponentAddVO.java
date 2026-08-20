package com.bmos.mes.service.equipment.vo;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yigaohui
 * @date 2024/4/23
 **/
@Data
@ApiModel("设备信息组件")
public class EquipmentInfoComponentAddVO extends BusinessDataHandleBaseDTO {
    @ApiModelProperty("设备id")
    private Long equipmentId;
}
