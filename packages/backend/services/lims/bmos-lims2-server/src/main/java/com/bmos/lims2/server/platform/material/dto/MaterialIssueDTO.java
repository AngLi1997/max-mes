package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialIssueDTO {

    /**
     * 物料平台id
     */
    private Long id;

    /**
     * 物料分类id
     */
    private Long materialCategoryId;

    /**
     * 所属物料id
     * FieldStrategy.IGNORED 更新时可以更新为null
     */
    private Long principalMaterialId;

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
     * 规格
     */
    private String specification;

    /**
     * 单位id
     */
    private Long unitId;


    /**
     * 是否是主要物料
     */
    private Boolean subMaterial;

    /**
     * 启停状态
     */
    private Boolean status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 已下发业务
     */
    private String dispenseRecord;

    /**
     * 扩展信息
     */
    private MaterialExpandInfoDTO expandInfo;
}
