package com.bmos.lims2.web.stability.scheme.vo.request;

import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 稳定性方案版本查询请求VO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("稳定性方案版本查询请求")
public class StabilitySchemeVersionQueryReqVO extends BasePage {

    @ApiModelProperty(value = "方案ID", required = true)
    private Long schemeId;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("状态")
    private StabilitySchemeVersionStatusEnum status;
}
