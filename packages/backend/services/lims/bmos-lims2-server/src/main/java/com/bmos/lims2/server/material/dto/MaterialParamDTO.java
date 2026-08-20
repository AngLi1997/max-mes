package com.bmos.lims2.server.material.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 检品sql查询参数
 */
@Getter
@Setter
@Accessors(chain = true)
public class MaterialParamDTO {

    /**
     * 平台物料id
     */
    private List<Long> platformMaterialIdList;

    /**
     * 业务分类
     */
    private Integer categoryType;

    /**
     * 检品名称
     */
    private String name;

    /**
     * 检品编码
     */
    private String mergeCode;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 规格
     */
    private String specification;

    /**
     * 分类id
     */
    private List<Long> categoryIdList;

    /**
     * 是否配置实验包
     */
    private Boolean packageFlag;

    /**
     * 排序
     */
    private String sortOrder;

    private Boolean status;

    /**
     * 是否绑定了启用的请验单
     */
    private Boolean hasEnabledDocument;

}
