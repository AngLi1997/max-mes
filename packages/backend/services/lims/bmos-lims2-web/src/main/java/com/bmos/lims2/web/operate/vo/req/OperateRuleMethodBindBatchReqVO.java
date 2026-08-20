package com.bmos.lims2.web.operate.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 按操作规程批量绑定方法 请求VO
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Getter
@Setter
@ApiModel("按操作规程批量绑定方法 请求VO")
public class OperateRuleMethodBindBatchReqVO {

    @ApiModelProperty(value = "操作规程ID", required = true)
    @NotNull(message = "操作规程ID不能为空")
    private Long operateId;

    @ApiModelProperty(value = "方法记录ID集合", required = true)
    private List<Long> recordIdList;
}


