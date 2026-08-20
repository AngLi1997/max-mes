package com.bmos.lims2.web.inspect.sample.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 样品批量领取请求VO
 * @Author: yigaohui
 * @Date: 2025/01/29 17:00
 */
@Getter
@Setter
@ApiModel("样品批量领取请求对象")
public class SampleBatchCollectionReqVO {

    @ApiModelProperty(value = "样品ID列表", required = true)
    @NotEmpty(message = "样品ID列表不能为空")
    private List<Long> sampleIds;

    @ApiModelProperty(value = "领取人ID", required = true)
    @NotNull(message = "领取人ID不能为空")
    private String collectorId;

    @ApiModelProperty(value = "领取人姓名", required = true)
    @NotNull(message = "领取人姓名不能为空")
    private String collectorName;

}
