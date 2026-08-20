package com.bmos.lims2.server.platform.material.dto;

import com.bmos.lims2.common.enums.CategoryInfoTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 产品id列表查询DTO
 */
@Getter
@Setter
public class MaterialIdListQueryDTO {

    /**
     * 产品分类
     */
    private CategoryInfoTypeEnum categoryInfoType;

    /**
     * 分类id
     */
    private Long categoryId;

}
