package com.bmos.mes.service.equipment.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备数采组件数据添加
 *
 * @author yigaohui
 * @date 2024/4/23
 **/
@Data
@ApiModel("设备数采组件数据添加")
public class EquipmentAcquisitionComponentAddVO extends BusinessDataHandleBaseDTO {

    @ApiModelProperty("设备id")
    @NotNull
    private Long equipmentId;

    @ApiModelProperty("设备数采-设备组的组件id")
    @NotNull
    private Long equipmentAcquisitionGroupComponentId;

    @ApiModelProperty("采集点信息 ")
    @NotEmpty
    private List<EquipmentAcquisitionPointVO> equipmentAcquisitionPoint;


    @ApiModelProperty("数采时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime acquisitionTime;

}
