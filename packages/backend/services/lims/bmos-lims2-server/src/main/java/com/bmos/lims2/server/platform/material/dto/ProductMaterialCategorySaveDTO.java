package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 保存物料分类DTO
 */
@Setter
@Getter
public class ProductMaterialCategorySaveDTO {

    /**
     * 分类名称 必传
     */
    private String name;

    /**
     * 分类编码 必传
     */
    private String code;

    /**
     * 父级Id
     */
    private Long parentId;

    /**
     * 类别信息类型 必传
     * {@link com.bmos.lims2.common.enums.CategoryInfoTypeEnum}
     */
    private Integer categoryType;

    /**
     * 业务注册
     */
    private boolean businessRegister;

    /**
     * 业务名称
     */
    private String businessName;
}
