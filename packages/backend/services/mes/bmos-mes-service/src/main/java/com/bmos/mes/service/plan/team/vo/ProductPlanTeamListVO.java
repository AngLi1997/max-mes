package com.bmos.mes.service.plan.team.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("生产班组列表VO")
@Data
public class ProductPlanTeamListVO {

    @ApiModelProperty("班组id")
    private Long id;

    @ApiModelProperty("班组名称")
    private String name;

    @ApiModelProperty("班组编码")
    private String code;

    @ApiModelProperty("班组描述")
    private String description;

    @ApiModelProperty("是否删除数据")
    private Boolean disabled;

    @ApiModelProperty("启用状态")
    private Boolean status;

    @ApiModelProperty("删除状态")
    private Boolean isDeleted;

}
