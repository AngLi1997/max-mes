package com.bmos.mes.service.product.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("生产物料分类查询DTO")
public class ProductMaterialCategoryQueryDTO {

    @ApiModelEnumProperty(value = "类别信息类型",required = true,enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    @NotNull
    private Integer categoryType;

    @ApiModelProperty(value = "关键字",required = false)
    private String keyword;

}
