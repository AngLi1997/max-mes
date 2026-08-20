package com.bmos.mes.service.audit.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "查询代办任务dto")
@Validated
public class FlowAuditTaskDTO {

    @ApiModelProperty(value = "分类")
    @NotBlank
    private String category;

    @ApiModelProperty(value = "分类集合")
    private List<String> categoryList;

    @ApiModelProperty(value = "业务id")
    private String businessKey;

    @ApiModelProperty(value = "业务id集合")
    private List<String> businessKeyList = new ArrayList<>();

    @ApiModelProperty(value = "页码")
    @NotNull
    private Integer current;

    @ApiModelProperty(value = "页数")
    @NotNull
    private Integer size;

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("排序规则")
    private String dir;

    private final String AUDIT_START_TIME_COLUMN = "processStartTime";
    private final String AUDIT_START_TIME_COLUMN2 = "process_start_time";

    public String getBusinessKeyOrderSql() {
        if (StrUtil.isBlank(this.orderBy)
                || StrUtil.equals(this.orderBy, AUDIT_START_TIME_COLUMN)
                || StrUtil.equals(this.orderBy, AUDIT_START_TIME_COLUMN2)) {
            return null;
        } else {
            if (StrUtil.isBlank(this.dir)) {
                return StrUtil.toUnderlineCase(this.orderBy);
            } else {
                return StrUtil.toUnderlineCase(this.orderBy + " " + this.dir);
            }
        }
    }
}
