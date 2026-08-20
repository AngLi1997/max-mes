package com.bmos.mes.service.plan.document.service.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录审批分页DTO
 */
@Getter
@Setter
@ApiModel("批记录审批分页DTO")
public class ArchiveAuditPageDTO extends AuditPageBaseQueryDTO {

    /**
     * 批记录模板名称
     */
    @ApiModelProperty("批记录模板名称")
    private String templateName;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    public boolean isExistsCondition(){
        return StrUtil.isNotEmpty(productName) || StrUtil.isNotEmpty(batchNo) || StrUtil.isNotEmpty(templateName);
    }

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.BATCH_RECORD_ARCHIVE);
    }
}
