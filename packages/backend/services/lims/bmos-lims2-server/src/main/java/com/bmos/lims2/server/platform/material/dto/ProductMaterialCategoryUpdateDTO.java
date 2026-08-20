package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * 生产物料分类更新DTO
 */
@Getter
@Setter
public class ProductMaterialCategoryUpdateDTO {

    /**
     * 必传
     */
    private Long id;

    /**
     * 分类名称
     * 必传
     */
    private String name;

}
