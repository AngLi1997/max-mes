package com.bmos.lims2.server.inspect.team.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("新建班组DTO")
public class InspectionTeamSaveDTO {

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    private String code;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("部门id")
    @NotEmpty
    private List<Long> deptIdList;
} 