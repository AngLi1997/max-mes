package com.bmos.mes.service.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备数采组件数据添加
 *
 * @author yigaohui
 * @date 2024/4/23
 **/
@Data
@ApiModel("设备数采组件数据修改")
public class EquipmentAcquisitionComponentModifyVO extends EquipmentAcquisitionComponentAddVO {

    @ApiModelProperty("备注")
    private String remark;
}
