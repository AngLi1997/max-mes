package com.bmos.mes.service.formula.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("根据工艺查询配方物料DTO")
public class ListProcessMaterialDTO {

    @ApiModelProperty("工艺版本id")
    @NotNull
    private Long processVersionId;

    @ApiModelProperty("物料类型")
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    private Integer categoryType;

}
