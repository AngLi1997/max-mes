package com.bmos.lims2.web.inspect.team.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("班组分配人员VO")
public class InspectionTeamAssignUserVO {

    @ApiModelProperty("班组id")
    @NotNull
    private Long id;

    @ApiModelProperty("人员id列表")
    private List<String> userIdList;
} 