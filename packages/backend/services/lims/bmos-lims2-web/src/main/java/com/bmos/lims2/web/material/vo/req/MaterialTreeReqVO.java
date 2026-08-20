package com.bmos.lims2.web.material.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 同步分类物料树查询DTO
 */
@Getter
@Setter
public class MaterialTreeReqVO {

    /**
     * 分类父级id
     */
    @ApiModelProperty("父级分类id")
    @NotNull
    private Long parentId;

    /**
     * 关键字 模糊搜索
     */
    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 类别信息类型 lims可以不传递（预留字段）
     * {@link com.bmos.lims2.common.enums.CategoryInfoTypeEnum}
     */
    @ApiModelProperty("类别信息")
    private Integer categoryType;

}
