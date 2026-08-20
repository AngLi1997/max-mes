package com.bmos.lims2.web.inspect.team.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("班组分页VO")
@Data
public class InspectionTeamPageReqVO extends BasePage {

    @ApiModelProperty("班组名称")
    private String name;

    @ApiModelProperty("班组编码")
    private String code;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;
} 