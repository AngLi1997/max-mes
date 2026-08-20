package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设备绑定采集点VO
 *
 * @className: AcquisitionBindVO
 * @author: yigaohui
 * @date: 2024/11/29 16:19
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("设备绑定采集点VO")
public class EquipmentAcquisitionBindVO {

    @ApiModelProperty(value = "采集点集合")
    @NotEmpty
    List<EquipmentBindAcquisitionVO> equipmentBindAcquisitionVOS;

    @ApiModelProperty(value = "数采平台")
    @NotNull
    private AcquisitionPlatformEnum acquisitionPlatform;
}
