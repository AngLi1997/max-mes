package com.bmos.lims2.server.report.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按检验单分页查询报告模板入参（根据单据获取方案版本）
 * @Author: yigaohui
 * @Date: 2025/09/08 10:00
 */
@Getter
@Setter
@ApiModel("按检验单分页查询报告模板入参")
public class ReportTemplateByOrderPageQueryDTO extends BasePage {

    @ApiModelProperty("检验单ID")
    private Long orderId;

    @ApiModelProperty("模板名称（模糊）")
    private String name;
}


