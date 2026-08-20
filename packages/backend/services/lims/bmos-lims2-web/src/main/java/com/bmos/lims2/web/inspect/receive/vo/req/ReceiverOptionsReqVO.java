package com.bmos.lims2.web.inspect.receive.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 批量样品查询领样人下拉请求
 * @Author: yigaohui
 * @Date: 2025/10/14 10:30
 */
@Getter
@Setter
@ApiModel("批量样品查询领样人下拉请求")
public class ReceiverOptionsReqVO {

    @ApiModelProperty(value = "样品ID列表", required = true)
    @NotEmpty(message = "样品ID列表不能为空")
    private List<Long> sampleIds;
}


