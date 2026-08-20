package com.bmos.mes.service.plan.document.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 生产批号的工艺绑定了哪些模板
 */
@Getter
@Setter
@ApiModel("查询生产批号的工艺绑定了哪些模板版本VO")
public class RecordArchiveTemplateVersionDTO extends BasePage {

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", required = true)
    @NotNull
    private Long planId;

    public BatchRecordArchiveQueryDTO convertArchiveQueryDTO() {
        BatchRecordArchiveQueryDTO res = new BatchRecordArchiveQueryDTO();
        res.setDir(getDir());
        res.setOrderBy(getOrderBy());
        res.setPageNum(getPageNum());
        res.setPageSize(getPageSize());
        return res;
    }

}
