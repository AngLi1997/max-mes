package com.bmos.lims2.web.stability.scheme.vo.request;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 稳定性方案查询请求VO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("稳定性方案查询请求")
public class StabilitySchemeQueryReqVO extends BasePage {

    @ApiModelProperty("方案名称")
    private String name;

    @ApiModelProperty("方案编码")
    private String code;

    @ApiModelProperty("物料分类ID（仅传分类时由后端解析启用物料ID集）")
    private Long categoryId;

    @ApiModelProperty("检品ID")
    private Long materialId;
}
