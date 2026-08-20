package com.bmos.wms.service.platform.material.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.wms.common.enums.inventory.CategoryInfoTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ApiModel("保存生产物料分类DTO")
public class ProductMaterialCategorySaveDTO {

    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "分类编码", required = true)
    @NotBlank
    private String code;

    @ApiModelProperty(value = "父级Id")
    private Long parentId;

    @ApiModelEnumProperty(value = "类别信息类型", required = true, enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    @NotNull
    private Integer categoryType;

    @ApiModelProperty(value = "业务注册")
    private boolean businessRegister;

    @ApiModelProperty("业务名称")
    private String businessName;
}
