package com.bmos.mes.service.process.vo;

import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.common.enums.process.AuditPerorationStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@ApiModel("工艺列表vo")
public class ProcedureConfirmVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "工序名称")
    private String procedureName;

    @ApiModelProperty(value = "工序完成时间")
    private LocalDateTime procedureTime;

    @ApiModelProperty("审批结论")
    private AuditPerorationStateEnum confirmOpinion;

    @ApiModelProperty("备注")
    private String remark;

}
