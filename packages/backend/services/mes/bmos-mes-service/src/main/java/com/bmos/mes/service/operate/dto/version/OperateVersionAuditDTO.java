package com.bmos.mes.service.operate.dto.version;

import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "操作规程dto")
public class OperateVersionAuditDTO extends AuditPageBaseQueryDTO {

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件编号")
    private String code;

    private List<Long> versionIds;

    private List<Long> deptIdList;

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.OPERATE_RULE_START);
    }
}
