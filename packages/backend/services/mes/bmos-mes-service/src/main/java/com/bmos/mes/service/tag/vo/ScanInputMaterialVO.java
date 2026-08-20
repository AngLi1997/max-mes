package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("扫描投入物料VO")
@Data
public class ScanInputMaterialVO {

    @ApiModelProperty("物料件id")
    private Long id;

    @ApiModelProperty("物料件号")
    private String no;

}
