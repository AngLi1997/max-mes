package com.bmos.mes.service.requisition.vo;

import com.bmos.mes.service.storage.manage.vo.BatchReservedMaterialVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("已预订暂存物料及仓库统计信息")
@Data
public class BatchReservedMaterialInfoVO {

    @ApiModelProperty("已预订暂存物料")
    private List<BatchReservedMaterialVO> reservedList;

    @ApiModelProperty("理论量合计(仓库批次)")
    private BigDecimal totalTheoreticalQuantity;

    @ApiModelProperty("计划量合计(仓库批次)")
    private BigDecimal totalPlannedQuantity;

}
