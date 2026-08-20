package com.bmos.lims2.web.stability.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案数据权限保存请求VO
 */
@Data
@ApiModel("稳定性方案数据权限保存请求")
public class StabilitySchemePermissionReqVO {

    @ApiModelProperty("部门ID列表")
    private List<Long> deptIds;
}
