package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("仓库可用物料量查询DTO")
@Data
public class InventoryAvailableQuantityQueryDTO {

    @ApiModelProperty("物料编码列表")
    private List<String> materialMergeCodeList;

    @ApiModelProperty("物料平台id列表")
    private List<Long> materialPlatformIdList;



}
