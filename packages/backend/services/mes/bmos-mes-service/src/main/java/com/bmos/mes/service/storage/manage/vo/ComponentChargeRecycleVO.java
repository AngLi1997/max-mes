package com.bmos.mes.service.storage.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("投料回收组件VO")
@Data
public class ComponentChargeRecycleVO {

    @ApiModelProperty("投料回收主键id(非componentId)")
    private Long chargeRecycleComponentId;

    @ApiModelProperty("已投料回收的列表")
    private List<ChargeRecycleListVO> list;

}
