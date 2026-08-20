package com.bmos.lims2.web.inspect.team.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 通过方案版本与检验项目查询班组人员-请求VO
 * @Author: yigaohui
 * @Date: 2025/11/20 00:00
 */
@Getter
@Setter
@ApiModel("通过方案版本与检验项目查询班组人员-请求VO")
public class TeamUserBySchemeItemReqVO {

    @ApiModelProperty(value = "方案版本ID", required = true)
    @NotNull(message = "方案版本ID不能为空")
    private Long schemeVersionId;

    @ApiModelProperty(value = "检验项目ID", required = true)
    @NotNull(message = "检验项目ID不能为空")
    private Long inspectItemId;
}


