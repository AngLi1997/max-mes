package com.bmos.mes.service.equipment.service.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备数采组件数据添加
 *
 * @author yigaohui
 * @date 2024/4/23
 **/
@Data
public class EquipmentAcquisitionComponentDTO extends BusinessDataHandleBaseDTO {

    private Long equipmentId;


    private List<EquipmentAcquisitionPointDTO> equipmentAcquisitionPoint;

    private String remark;

    private Long equipmentAcquisitionGroupComponentId;

    private LocalDateTime acquisitionTime;
}
