package com.bmos.mes.service.formula.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("分页查询DTO")
@Getter
@Setter
public class ProductFormulaAuditPageQueryDTO extends AuditPageBaseQueryDTO {

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("配方名称")
    private String formulaName;

    @ApiModelProperty(hidden = true)
    public boolean isExistsSearchCondition() {
        return StrUtil.isNotEmpty(productName) || StrUtil.isNotEmpty(formulaName);
    }

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.PRODUCT_FORMULA);
    }

}
