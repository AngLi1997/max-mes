package com.bmos.lims2.server.inspect.team.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("班组分页DTO")
@Data
public class InspectionTeamPageReqDTO extends BasePage {

    @ApiModelProperty("班组名称")
    private String name;

    @ApiModelProperty("班组编码")
    private String code;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;
} 