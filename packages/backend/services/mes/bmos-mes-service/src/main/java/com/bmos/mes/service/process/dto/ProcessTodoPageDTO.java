package com.bmos.mes.service.process.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工艺流程待办DTO")
public class ProcessTodoPageDTO extends AuditPageBaseQueryDTO {

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("工艺名称")
    private String processName;

    private List<Long> processVersionIds;


    @JsonIgnore
    public Boolean isExistsSearchCondition() {
        return StrUtil.isNotEmpty(processName) || StrUtil.isNotEmpty(productName);
    }

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.PROCESS);
    }

}
