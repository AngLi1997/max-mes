package com.bmos.mes.service.lotrelease.manage.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批签发查询审核分页请求
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 11:37
 */
@ApiModel("批签发查询审核分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class LotReleaseAuditPageQuery extends AuditPageBaseQueryDTO {

    @ApiModelProperty(value = "批签发模板", example = "1")
    private String templateName;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "生产批号", example = "1")
    private String batchNo;

    public boolean isExistsCondition(){
        return productId != null || StrUtil.isNotEmpty(batchNo) || StrUtil.isNotEmpty(templateName);
    }

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.BATCH_SIGNATURE);
    }

}
