package com.bmos.lims2.server.material.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分类请求参数
 */
@Getter
@Setter
@Accessors(chain = true)
public class MaterialCategoryParamDTO {

    /**
     * 平台物料id集合
     */
    private List<Long> platformCategoryIdList;

    /**
     * lims对于检品信息的分类（非平台的物料分类）
     */
    private Integer categoryType;

    /**
     * 是否查询出parentId为空的数据
     */
    private Boolean prentIdEmpty;

    /**
     * 关键字 模糊查询
     */
    private String keyword;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 排序字段
     */
    private String order;
}
