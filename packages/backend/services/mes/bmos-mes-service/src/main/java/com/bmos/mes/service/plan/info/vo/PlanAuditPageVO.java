package com.bmos.mes.service.plan.info.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import com.bmos.mes.service.utils.BigDecimalFormatUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("PlanAuditPageVO:生产计划审核分页VO")
public class PlanAuditPageVO extends TaskListResp {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("生产时间")
    private LocalDate productDate;

    @ApiModelProperty("计划类型")
    private ProductPlanTypeEnum type;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("流程发起人")
    private String processStartBy;

    @ApiModelProperty("流程发起人对象")
    private String processStartByName;

    @ApiModelProperty("流程实例")
    private String processInstanceId;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    public String getProcessStartByName() {
        BaseUserDO user = UserUtils.getUser(processStartBy);
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName() + StrUtil.DASHED + user.getLoginName()) : "";
    }

    public String getBatchQuantity(){
        return BigDecimalFormatUtil.formatBigDecimal(batchQuantity);
    }

}
