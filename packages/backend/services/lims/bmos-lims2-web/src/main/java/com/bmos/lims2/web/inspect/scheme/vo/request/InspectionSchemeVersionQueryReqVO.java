package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * 检验方案版本查询请求VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案版本查询请求")
public class InspectionSchemeVersionQueryReqVO extends BasePage {

    @ApiModelProperty(value = "方案ID", required = true)
    @NotNull(message = "方案ID不能为空")
    private Long schemeId;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效")
    @Pattern(regexp = "^(EDITING|APPROVING|ACTIVE|INACTIVE)$", message = "版本状态不正确")
    private String status;
} 