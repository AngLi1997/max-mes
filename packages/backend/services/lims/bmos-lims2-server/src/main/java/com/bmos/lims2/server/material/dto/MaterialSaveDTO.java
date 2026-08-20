package com.bmos.lims2.server.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("检品新增DTO")
@Data
public class MaterialSaveDTO {

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty(value = "检品名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "检品编码", required = true)
    @NotBlank
    private String code;

    @ApiModelProperty(value = "检品规格", required = true)
    @NotBlank
    private String specification;

    @ApiModelProperty(value = "检品单位id", required = true)
    @NotNull
    private Long unitId;


    @ApiModelProperty(value = "检品单位扩展id", required = false)
    private Long extendUnitId;

    @ApiModelProperty(value = "是否是成员物料/成员产品", required = true)
    @NotNull
    private Boolean subMaterial;

    @ApiModelProperty("所属物料id")
    private Long principalMaterialId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("自定义字段DTO")
    private List<MaterialFieldSaveDTO> fieldList;


}
