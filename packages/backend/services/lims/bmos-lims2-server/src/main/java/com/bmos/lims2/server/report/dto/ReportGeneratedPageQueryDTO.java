package com.bmos.lims2.server.report.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 已生成报告分页查询参数
 * @Author: yigaohui
 * @Date: 2025/09/02 00:00
 */
@Getter
@Setter
@ApiModel("已生成报告分页查询参数")
public class ReportGeneratedPageQueryDTO extends BasePage {

    @ApiModelProperty("报告模板ID")
    private Long templateId;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;
}


