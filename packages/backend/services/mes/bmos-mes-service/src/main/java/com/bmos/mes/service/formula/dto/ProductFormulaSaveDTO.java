package com.bmos.mes.service.formula.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("新增配方DTO")
@Data
public class ProductFormulaSaveDTO {

    @ApiModelProperty("配方名称")
    @NotEmpty
    private String name;

    @ApiModelProperty("配方版本")
    @NotEmpty
    private String versionNo;

    @ApiModelProperty("产品id")
    @NotNull
    private Long productId;

    @ApiModelProperty("物料列表")
    @NotEmpty
    private List<ProductFormulaMaterialDTO> materialList;

    @ApiModelProperty("批量")
    @NotNull
    private BigDecimal batchQuantity;

    @ApiModelProperty("单位id")
    @NotNull
    private Long unitId;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("数据权限部门id列表")
    private List<Long> deptIds;




}
