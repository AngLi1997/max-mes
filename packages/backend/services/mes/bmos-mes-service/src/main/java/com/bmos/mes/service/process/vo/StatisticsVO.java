package com.bmos.mes.service.process.vo;

import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.common.enums.process.AuditPerorationStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel("报表统计vo")
public class StatisticsVO {

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("状态")
    private AuditPerorationStateEnum confirmOpinion;
}
