package com.bmos.mes.service.product.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("同步物料信息DTO")
public class SyncMaterialInfoDTO {

    @ApiModelProperty("物料ids")
    List<Long> materialIds;

    @ApiModelProperty("物料分类ids")
    @NotEmpty
    List<Long> materialCategoryIds;

    @ApiModelEnumProperty(value = "业务信息类型",required = true,enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    @NotNull
    private Integer categoryType;
}
