package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 生产物料保存DTO
 */
@Getter
@Setter
public class ProductMaterialSaveDTO {

    /**
     * 物料分类id 必传
     */
    private Long materialCategoryId;

    /**
     * 所属物料id
     */
    private Long principalMaterialId;

    /**
     * 名称 必传
     */
    private String name;

    /**
     * 编码 必传
     */
    private String code;

    /**
     * 规格 必传
     */
    private String specification;


    /**
     * 单位id 必传
     */
    private Long unitId;

    /**
     * 拓展单位id
     */
    private Long unitExtendId;

    /**
     * 是否是成员物料/成员产品 必传
     */
    private Boolean subMaterial;

    /**
     * 是否是成品
     */
    private Boolean finishProduct;

    /**
     * 备注
     */
    private String remark;

    /**
     * 生产周期(天)
     */
    private Integer productionCycle;

    /**
     * 内包规格
     */
    private String innerPackingSpecification;

    /**
     * 包装规格
     */
    private String packingSpecification;

    /**
     * 业务注册
     */
    private Boolean businessRegister;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 拓展信息
     */
    private MaterialExpandInfoDTO expandInfo;
}
