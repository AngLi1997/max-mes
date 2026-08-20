package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.process.AuditPerorationStateEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@TableName(value = "bm_procedure_confirm")
public class ProcedureConfirm extends BaseDO {

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("结论填报时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty("工序完成时间")
    private LocalDateTime procedureTime;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺结论id")
    private Long processConfirmId;

    @ApiModelProperty("审批结论")
    private AuditPerorationStateEnum confirmOpinion;

    @ApiModelProperty("备注")
    private String remark;
}
