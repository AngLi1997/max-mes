package com.bmos.mes.service.equipment.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author yigaohui
 * @date 2024/4/23
 **/
@Data
@ApiModel("设备信息组件修改")
public class EquipmentInfoComponentModifyVO extends EquipmentInfoComponentAddVO {
    private String remark;
}
