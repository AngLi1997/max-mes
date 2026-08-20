package com.bmos.lims2.web.report.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按方案/方案版本分页查询报告模板-入参
 * @Author: yigaohui
 * @Date: 2025/09/08 10:10
 */
@Getter
@Setter
@ApiModel("按方案/方案版本分页查询报告模板-入参")
public class ReportTemplateBySchemePageReqVO extends BasePage {

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("方案版本ID（优先按版本筛选）")
    private Long schemeVersionId;

    @ApiModelProperty("模板名称（模糊）")
    private String name;
}


