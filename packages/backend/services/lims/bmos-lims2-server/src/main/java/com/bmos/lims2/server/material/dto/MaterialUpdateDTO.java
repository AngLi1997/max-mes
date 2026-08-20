package com.bmos.lims2.server.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 检品编辑参数VO
 */
@Getter
@Setter
@ApiModel("检品编辑参数VO")
public class MaterialUpdateDTO {

    @ApiModelProperty(value = "检品id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "描述")
    @Length(max = 100)
    private String remark;

    @ApiModelProperty("单位id")
    @NotNull
    private Long unitId;


    @ApiModelProperty(value = "检品单位扩展id", required = false)
    private Long extendUnitId;

    @ApiModelProperty("自定义字段DTO")
    private List<MaterialFieldSaveDTO> fieldList;

}
