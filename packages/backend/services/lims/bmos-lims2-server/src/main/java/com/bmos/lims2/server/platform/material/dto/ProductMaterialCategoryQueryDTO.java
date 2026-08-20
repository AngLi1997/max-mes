package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 生产物料分类查询DTO
 */
@Getter
@Setter
public class ProductMaterialCategoryQueryDTO {

    /**
     * 类别信息类型
     * {@link com.bmos.lims2.common.enums.CategoryInfoTypeEnum}
     */
    private Integer categoryType;

    /**
     * 关键字 模糊查询
     */
    private String keyword;

}
