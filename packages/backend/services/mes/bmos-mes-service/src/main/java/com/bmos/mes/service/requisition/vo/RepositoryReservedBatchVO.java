package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("仓库预定批次信息及暂存领料信息")
public class RepositoryReservedBatchVO {

    @ApiModelProperty("已预订仓库批次")
    private List<RepositoryMaterialReservedBatchListVO> batchList;

    @ApiModelProperty("理论量合计(仓库批次)")
    private BigDecimal totalTheoreticalQuantity;

    @ApiModelProperty("计划量合计(仓库批次)")
    private BigDecimal totalPlannedQuantity;

    @ApiModelProperty("已预定暂存理论量")
    private BigDecimal storageTheoreticalQuantity;

}
