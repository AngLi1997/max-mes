package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 生产物料编辑DTO
 */
@Getter
@Setter
public class ProductMaterialUpdateDTO {
    /**
     * id 必传
     */
    private Long id;

    /**
     * 拓展单位id 必传
     */
    private Long unitExtendId;

    /**
     * 是否是成品 必传
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
     * 拓展信息
     */
    private MaterialExpandInfoDTO expandInfo;

}
