package com.bmos.lims2.web.inspect.receive.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 样品批量接收请求VO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@ApiModel("样品批量接收请求")
public class SampleBatchReceiveReqVO {

    @ApiModelProperty(value = "样品ID列表", required = true)
    @NotEmpty(message = "样品ID列表不能为空")
    private List<Long> sampleIds;
}
