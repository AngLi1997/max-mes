package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 检验方案版本审批查询请求VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@ApiModel("检验方案版本审批查询请求VO")
public class InspectionSchemeVersionAuditQueryReqVO {

    @ApiModelProperty("方案名称")
    private String schemeName;

    @ApiModelProperty("物料ID列表")
    private List<Long> materialIds;

    @ApiModelProperty(value = "页码", required = true)
    @NotNull(message = "页码不能为空")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小", required = true)  
    @NotNull(message = "页大小不能为空")
    private Integer pageSize = 20;

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("排序方向")
    private String dir;
}