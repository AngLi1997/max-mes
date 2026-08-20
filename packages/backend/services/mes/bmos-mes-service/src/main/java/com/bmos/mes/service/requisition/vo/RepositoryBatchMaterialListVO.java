package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("仓库批次物料件列表VO")
@Data
public class RepositoryBatchMaterialListVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("出库量")
    private BigDecimal quantity;

    @ApiModelProperty("单位")
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("暂存货位id")
    private Long cargoPositionId;

    @ApiModelProperty("暂存货位名称")
    private String cargoPositionName;

    @ApiModelProperty("平台物料id")
    private String platformMaterialId;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料合并编码")
    private String materialMergeCode;



}
