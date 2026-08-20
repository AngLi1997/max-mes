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
@ApiModel(value = "分类修改Dto")
public class CategoryUpdateDTO {

    @ApiModelProperty("分类名称")
    @NotBlank
    private String name;

    @ApiModelProperty("主键id")
    @NotBlank
    private String id;

}
