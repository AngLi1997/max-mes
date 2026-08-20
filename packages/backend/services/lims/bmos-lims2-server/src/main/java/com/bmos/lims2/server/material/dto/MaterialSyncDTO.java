package com.bmos.lims2.server.material.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.lims2.common.enums.CategoryInfoTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("物料同步请求参数")
public class MaterialSyncDTO {

    @ApiModelProperty("物料ids")
    List<Long> materialIds;

    @ApiModelProperty("物料分类ids")
    List<Long> materialCategoryIds;

    @ApiModelEnumProperty(value = "业务信息类型",required = true,enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    @NotNull
    private Integer categoryType;

}
