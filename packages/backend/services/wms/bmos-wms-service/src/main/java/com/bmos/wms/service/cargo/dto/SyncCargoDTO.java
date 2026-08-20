package com.bmos.wms.service.cargo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * SyncCargoDTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/25 23:09
 */
@Data
@ApiModel("同步货品dto")
public class SyncCargoDTO {

    /**
     * 物料ids
     */
    @ApiModelProperty("物料ids")
    private List<Long> materialIds;

    /**
     * 物料分类ids
     */
    @ApiModelProperty("物料分类ids")
    private List<Long> materialCategoryIds;
}
