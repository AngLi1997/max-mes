package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("导出设备状态日志入参")
public class EquipmentStatusExportDTO extends EquipmentStatusLogPageDTO{

    /**
     * 是否导出
     */
    @ApiModelProperty("是否导出筛选项")
    private Boolean all;

}
