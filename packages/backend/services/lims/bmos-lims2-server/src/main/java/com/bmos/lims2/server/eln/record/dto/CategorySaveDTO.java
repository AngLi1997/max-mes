package com.bmos.lims2.server.eln.record.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@ApiModel(value = "分类保存Dto")
public class CategorySaveDTO {

    @ApiModelProperty("分类名称")
    @NotBlank
    private String name;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("标识")
    private String code;

    @ApiModelProperty("id")
    private Long id;

}
