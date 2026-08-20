package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 关联物料查询DTO
 */
@Getter
@Setter
@ToString
public class MaterialPrincipalQueryDTO {

    /**
     * 物料分类id
     */
    private Long materialCategoryId;

    /**
     * 是否过滤
     */
    private Boolean filter;

}
