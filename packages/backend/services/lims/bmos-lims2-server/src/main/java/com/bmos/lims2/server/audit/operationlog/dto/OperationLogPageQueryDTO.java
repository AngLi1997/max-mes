package com.bmos.lims2.server.audit.operationlog.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("操作日志分页查询DTO")
public class OperationLogPageQueryDTO extends BasePage {

    @ApiModelProperty("业务模块")
    private String module;

    @ApiModelProperty("业务数据id")
    private Long businessId;
}
