package com.bmos.platform.service.material.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 下发给业务的物料VO
 */
@Getter
@Setter
public class IssueMaterialVO {

    /**
     * 物料id
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
    @TableField(updateStrategy = FieldStrategy.IGNORED)
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
    @TableField("is_sub_material")
    private Boolean subMaterial;

    private Boolean status;


    private String remark;

    /**
     * 临期日期
     */
    private Integer dyingPeriod;

}
