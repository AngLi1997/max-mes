package com.bmos.lims2.server.material.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 同步分类物料树查询DTO
 */
@Getter
@Setter
public class MaterialCategoryTreeQueryDTO extends MaterialCategoryQueryDTO{

    /**
     * 分类父级id
     */
    @ApiModelProperty("父级分类id")
    @NotNull
    private Long parentId;

}
