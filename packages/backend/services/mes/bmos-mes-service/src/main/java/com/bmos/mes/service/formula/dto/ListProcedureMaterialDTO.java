package com.bmos.mes.service.formula.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("查询工序步骤物料")
@Data
public class ListProcedureMaterialDTO {

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("物料类型")
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    private Integer categoryType;

    @ApiModelProperty("工步模型id")
    private Long stepModelId;

}
