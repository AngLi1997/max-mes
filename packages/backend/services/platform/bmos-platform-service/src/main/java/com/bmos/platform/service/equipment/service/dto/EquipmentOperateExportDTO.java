package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("导出操作日志入参")
public class EquipmentOperateExportDTO extends EquipmentOperateLogPageDTO{

    /**
     * 是否导出
     */
    private Boolean all;

}
