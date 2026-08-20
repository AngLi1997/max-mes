package com.bmos.lims2.server.platform.material.dto;

import com.bmos.mybatis.page.BasePage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 物料分页查询DTO
 */
@Getter
@Setter
@ToString
public class ProductMaterialPageQueryDTO extends BasePage {

    /**
     * 物料分类id
     */
    private Long materialCategoryId;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 合并编码
     */
    private String mergeCode;

    /**
     * 类别信息类型 必传
     * {@link com.bmos.lims2.common.enums.CategoryInfoTypeEnum}
     */
    private Integer categoryType;

    /**
     * 启停状态
     */
    private Boolean status;

    /**
     * 物料分类id 集合
     */
    private List<Long> materialCategoryIds;
}
