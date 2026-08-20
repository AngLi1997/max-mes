package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("扫描物料件或设备VO")
@Data
public class ScanMaterialOrDeviceVO {

    @ApiModelProperty("物料信息")
    private ChargeRecycleMaterialVO materialInfo;

}
