package com.bmos.lims2.web.inspect.parameter.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 分析项-方法 批量绑定请求VO
 * @Author: yigaohui
 * @Date: 2025/10/31 11:30
 */
@Getter
@Setter
@ApiModel("分析项-方法 批量绑定请求VO")
public class InspectMethodBindBatchReqVO {

    @ApiModelProperty(value = "分析项ID", required = true)
    @NotNull(message = "分析项ID不能为空")
    private Long parameterId;

    /**
     * 方法ID列表，可为空；为空表示解除当前分析项下的所有方法绑定关系
     */
    @ApiModelProperty(value = "方法ID列表，空则表示解除绑定", required = false)
    private List<Long> recordIdList;
}


