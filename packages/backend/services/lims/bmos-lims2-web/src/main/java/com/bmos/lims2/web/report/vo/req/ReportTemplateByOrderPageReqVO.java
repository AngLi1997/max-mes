package com.bmos.lims2.web.report.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按检验单分页查询报告模板-入参
 * @Author: yigaohui
 * @Date: 2025/09/08 10:10
 */
@Getter
@Setter
@ApiModel("按检验单分页查询报告模板-入参")
public class ReportTemplateByOrderPageReqVO extends BasePage {

    @ApiModelProperty("检验单ID")
    private Long orderId;

    @ApiModelProperty("模板名称（模糊）")
    private String name;
}


