package com.bmos.wms.service.inventory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("仓库批次查询DTO")
@Data
public class InventoryBatchQueryDTO {

    @ApiModelProperty("物料编码列表")
    private List<String> materialMergeCodeList;

    @ApiModelProperty("物料平台id列表")
    private List<Long> materialPlatformIdList;

}
