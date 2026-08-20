package com.bmos.lims2.server.audit.dto;

import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuditPageBaseQueryDTO extends BasePage{

    @ApiModelProperty(value = "页码")
    @NotNull
    private Integer pageNum;

    @ApiModelProperty(value = "页数")
    @NotNull
    private Integer pageSize;

    private String orderBy;

    private String dir;

    public BasePage convertBasePage() {
        BasePage basePage = new BasePage();
        basePage.setPageNum(pageNum);
        basePage.setPageSize(pageSize);
        return basePage;
    }

    public FlowAuditTaskDTO convertAuditTaskDTO (AuditCategoryCodeEnum category){
        FlowAuditTaskDTO flowAuditTaskDTO = new FlowAuditTaskDTO();
        flowAuditTaskDTO.setCurrent(pageNum);
        flowAuditTaskDTO.setSize(pageSize);
        flowAuditTaskDTO.setOrderBy(orderBy);
        flowAuditTaskDTO.setDir(dir);
        flowAuditTaskDTO.setCategory(category.getCode());
        return flowAuditTaskDTO;
    }


}
