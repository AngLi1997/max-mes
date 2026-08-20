package com.bmos.mes.service.product.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("工艺产品VO")
@Data
public class ProcessProductVO {

    /**
     * 产品id
     */
    private Long id;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 物料分类id
     */
    private Long materialCategoryId;

    /**
     * 所属物料id
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
     * 规格
     */
    private String specification;

    /**
     * 临期天数
     */
    private Integer dyingPeriod;

    /**
     * 保存条件
     */
    private String storageCondition;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 拓展单位id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long unitExtendId;

    /**
     * 分类类型冗余字段
     */
    @TableField(exist = false)
    private Integer categoryType;

    /**
     * 是否是成员物料
     */
    @TableField("is_sub_material")
    private Boolean subMaterial;

    /**
     * 是否是成品
     */
    @TableField("is_finish_product")
    private Boolean finishProduct;

    /**
     * 合并编码
     */
    private String mergeCode;

    private Boolean status;

    private String remark;

    /**
     * 产品标识
     */
    private String productMark;

    /**
     * 关联的平台的物料id
     */
    @TableField("platform_material_id")
    private Long platformMaterialId;

    @ApiModelProperty(value = "生产周期(天)")
    private Integer productionCycle;

    @ApiModelProperty(value = "内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty(value = "包装规格")
    private String packingSpecification;

    @TableField(value = "expand_info", typeHandler = JacksonTypeHandler.class)
    private MaterialExpandInfo expandInfo;

    @JsonIgnore
    public Long getFinalUnitId() {
        return unitExtendId == null ? unitId : unitExtendId;
    }


}
