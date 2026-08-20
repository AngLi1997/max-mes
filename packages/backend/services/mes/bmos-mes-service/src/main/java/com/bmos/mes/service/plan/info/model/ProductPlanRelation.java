package com.bmos.mes.service.plan.info.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.BooleanEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
* 生产计划关联关系表
*/
@Getter
@Setter
@SuperBuilder
@With
@AllArgsConstructor
@ToString
@TableName(value = "bm_product_plan_relation")
public class ProductPlanRelation {
    @Tolerate
    public ProductPlanRelation() {}
    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("工序id")
    private Long processId;

    @ApiModelProperty("关联生产计划id")
    private Long relationProductPlanId;

    @ApiModelProperty("是否直接关联")
    private BooleanEnum isDirectRelation;

    /**
     * 关联来源
     * 在间接关联时
     * 需要保存由哪个计划关联产生的间接关联
     * 
     */
    @ApiModelProperty("关联来源")
    private Long sourceProductPlanId;
}
