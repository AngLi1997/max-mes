package com.bmos.platform.service.equipment.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 采集点关联设备数据
 *
 * @author yigaohui
 * @date 2024/8/15
 **/
@Data
public class AcquisitionPointEquipmentDataVO {

    @NotEmpty(message = "{res_8112038}")
    private List<Long> acquisitionPointList;

    @NotEmpty(message = "{res_8112039}")
    private String equipmentTagDataCode;
}
