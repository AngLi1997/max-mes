package com.bmos.mes.service.process.dto.query;

import com.bmos.mes.common.enums.StateEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("工艺查询DTO")
public class ProcessQueryDTO {

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelEnumProperty(value = "启用状态",enumClass = StateEnum.class)
    private Boolean state;

    @ApiModelProperty("确认状态")
    private String actionState;
}
