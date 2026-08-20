package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("领料单列表VO")
@Data
public class RequisitionListVO {

    @ApiModelProperty("领料单id")
    private Long id;

    @ApiModelProperty("领料单名称")
    private String name;

}
