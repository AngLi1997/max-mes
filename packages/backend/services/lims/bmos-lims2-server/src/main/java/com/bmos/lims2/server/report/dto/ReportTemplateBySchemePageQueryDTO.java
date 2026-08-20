package com.bmos.lims2.server.report.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按方案/方案版本分页查询报告模板入参
 * @Author: yigaohui
 * @Date: 2025/09/08 10:00
 */
@Getter
@Setter
@ApiModel("按方案/方案版本分页查询报告模板入参")
public class ReportTemplateBySchemePageQueryDTO extends BasePage {

    @ApiModelProperty("方案ID（传入则按该方案下所有版本绑定的模板筛选）")
    private Long schemeId;

    @ApiModelProperty("方案版本ID（优先按版本筛选，忽略schemeId）")
    private Long schemeVersionId;

    @ApiModelProperty("模板名称（模糊）")
    private String name;
}


