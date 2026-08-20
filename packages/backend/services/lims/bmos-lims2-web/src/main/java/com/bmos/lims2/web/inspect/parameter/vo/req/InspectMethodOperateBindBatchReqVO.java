package com.bmos.lims2.web.inspect.parameter.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 按方法批量绑定操作规程 请求VO
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Getter
@Setter
@ApiModel("按方法批量绑定操作规程 请求VO")
public class InspectMethodOperateBindBatchReqVO {

    @ApiModelProperty(value = "方法记录ID", required = true)
    @NotNull(message = "方法记录ID不能为空")
    private Long recordId;

    @ApiModelProperty(value = "操作规程ID集合", required = true)
    private List<Long> operateIdList;
}


