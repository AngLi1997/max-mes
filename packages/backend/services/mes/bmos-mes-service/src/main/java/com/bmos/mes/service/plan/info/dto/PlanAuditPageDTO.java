package com.bmos.mes.service.plan.info.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("PlanAuditPageDTO:生产计划审核分页列表查询条件DTO")
public class PlanAuditPageDTO extends AuditPageBaseQueryDTO {
    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("计划类型")
    private String type;

    public boolean isExistsSearchCondition() {
        return StrUtil.isNotEmpty(planNo) || StrUtil.isNotEmpty(productName)
            || StrUtil.isNotEmpty(processName) || StrUtil.isNotEmpty(batchNo)
            || StrUtil.isNotEmpty(type);
    }

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.PRODUCT_PLAN);
    }
}
