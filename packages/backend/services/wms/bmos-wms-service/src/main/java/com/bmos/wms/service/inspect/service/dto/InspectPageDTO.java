package com.bmos.wms.service.inspect.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("WMS 检验单分页DTO")
public class InspectPageDTO extends BasePage {

    @ApiModelProperty("库存批次id（bw_inventory_batch.id）")
    private Long inventoryBatchId;

    @ApiModelProperty("LIMS 检验单号")
    private String inspectNo;

    @ApiModelProperty("状态 1请验中 2已完成 3已退回")
    private Integer status;
}
