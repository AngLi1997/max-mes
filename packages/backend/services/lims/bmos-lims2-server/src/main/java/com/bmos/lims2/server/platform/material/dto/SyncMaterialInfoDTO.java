package com.bmos.lims2.server.platform.material.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("同步物料信息DTO")
public class SyncMaterialInfoDTO {

    /**
     * 物料ids
     */
    List<Long> materialIds;

    /**
     * 物料分类ids 必传
     */
    List<Long> materialCategoryIds;

    /**
     * 业务信息类型 必传
     * {@link com.bmos.lims2.common.enums.CategoryInfoTypeEnum}
     */
    private Integer categoryType;
}
