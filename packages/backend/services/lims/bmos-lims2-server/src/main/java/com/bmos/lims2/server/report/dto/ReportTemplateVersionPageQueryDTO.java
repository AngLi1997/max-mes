package com.bmos.lims2.server.report.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 报告模板版本分页查询参数
 * @Author: yigaohui
 * @Date: 2025/09/05  
 */
@Getter
@Setter
@ApiModel("报告模板版本分页查询参数")
public class ReportTemplateVersionPageQueryDTO extends BasePage {

    @ApiModelProperty("模板ID")
    private Long templateId;
}


