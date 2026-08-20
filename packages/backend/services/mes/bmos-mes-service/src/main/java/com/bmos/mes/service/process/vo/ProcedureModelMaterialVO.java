package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName ProcedureModelMaterialVO
 * @Author Ren Jin Guang
 * @Date 2024/12/19 10:22
 */
@Getter
@Setter
@ToString
public class ProcedureModelMaterialVO {

    @ApiModelProperty("工序id")
    private Long procedureModelId;

    @ApiModelProperty("配方物料id")
    private Long productFormulaMaterialId;

    @ApiModelProperty("工序名称")
    private String procedureModelName;
}
