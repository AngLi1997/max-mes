package com.bmos.mes.service.weigh.free.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 自由称量结果VO
 * @author liang
 * @version 1.0.0
 * @date 2025/2/25 10:02
 */
@ApiModel("自由称量结果VO")
@Data
public class FreeWeighResult {

    @ApiModelProperty(value = "物料件号", example = "1")
    private String storageMaterialNo;

    @ApiModelProperty(value = "皮重", example = "1.00")
    private BigDecimal tareWeight;

    @ApiModelProperty(value = "毛重", example = "1.00")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "净重", example = "1.00")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;


    @ApiModelProperty(value = "容器", example = "容器编码-容器名称")
    private String containerName;

    @ApiModelProperty(value = "货位", example = "货位编码-货位名称")
    private String cargoPositionName;
}
