package com.bmos.mes.service.process.vo;

import cn.hutool.core.util.ObjectUtil;
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
@ApiModel("工序审批查询结果")
public class AuditOpinionVO {

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String planBatchNo;

    @ApiModelProperty("生产开始时间")
    private String startTime;

    @ApiModelProperty("生产结束时间时间")
    private String endTime;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工序完成时间")
    private String procedureTime;

    @ApiModelProperty("审批备注")
    private String remark;

    @ApiModelProperty("审批结论")
    private AuditPerorationStateEnum confirmOpinion;
}
